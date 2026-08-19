package com.threadverse_backend.service.order;

import com.threadverse_backend.dto.request.CreateOrderRequest;
import com.threadverse_backend.dto.request.UpdateOrderStatusRequest;
import com.threadverse_backend.dto.response.OrderResponse;
import com.threadverse_backend.entity.*;
import com.threadverse_backend.enums.OrderStatus;
import com.threadverse_backend.enums.PaymentMethod;
import com.threadverse_backend.enums.PaymentStatus;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.order.OrderMapper;
import com.threadverse_backend.repository.address.AddressRepository;
import com.threadverse_backend.repository.cart.CartRepository;
import com.threadverse_backend.repository.order.OrderRepository;
import com.threadverse_backend.repository.payment.PaymentRepository;
import com.threadverse_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal COD_DELIVERY_CHARGE =
            new BigDecimal("49.00");

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderResponse createOrder(
            Long userId,
            CreateOrderRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Address address = addressRepository.findById(
                        request.getAddressId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        if (!address.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Address does not belong to this user"
            );
        }

        Cart cart = cartRepository
                .findByUserUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        )
                );

        if (cart.getCartItems() == null ||
                cart.getCartItems().isEmpty()) {

            throw new BadRequestException(
                    "Cart is empty"
            );
        }

        /*
         * Product subtotal only.
         */
        BigDecimal subtotal = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .address(address)
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        for (CartItem cartItem : cart.getCartItems()) {

            ProductVariant variant =
                    cartItem.getVariant();

            if (!Boolean.TRUE.equals(
                    variant.getAvailable()
            )) {

                throw new BadRequestException(
                        "Product variant is not available"
                );
            }

            if (variant.getStock() <
                    cartItem.getQuantity()) {

                throw new BadRequestException(
                        "Insufficient stock for "
                                + variant.getProduct().getName()
                );
            }

            BigDecimal unitPrice =
                    variant.getProduct().getPrice();

            BigDecimal itemTotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );

            subtotal =
                    subtotal.add(itemTotal);

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(order)
                            .variant(variant)
                            .productName(
                                    variant.getProduct().getName()
                            )
                            .productSize(
                                    variant.getSize()
                            )
                            .unitPrice(unitPrice)
                            .quantity(
                                    cartItem.getQuantity()
                            )
                            .build();

            order.getOrderItems().add(
                    orderItem
            );
        }

        /*
         * COD gets an additional ₹49 delivery charge.
         * Online payment gets free delivery.
         */
        BigDecimal deliveryCharge =
                request.getPaymentMethod() ==
                        PaymentMethod.COD
                        ? COD_DELIVERY_CHARGE
                        : BigDecimal.ZERO;

        /*
         * Final amount = product subtotal + delivery.
         */
        BigDecimal finalAmount =
                subtotal.add(deliveryCharge);

        order.setTotalAmount(finalAmount);

        Order savedOrder =
                orderRepository.save(order);

        /*
         * Payment amount must exactly match
         * the final order amount.
         */
        Payment payment =
                Payment.builder()
                        .order(savedOrder)
                        .amount(
                                savedOrder.getTotalAmount()
                        )
                        .paymentMethod(
                                savedOrder.getPaymentMethod()
                        )
                        .paymentStatus(
                                PaymentStatus.PENDING
                        )
                        .build();

        paymentRepository.save(payment);

        /*
         * COD is confirmed immediately.
         *
         * Order  -> CONFIRMED
         * Payment -> PENDING
         *
         * Online payments remain PENDING until
         * Razorpay verification succeeds.
         */
        if (savedOrder.getPaymentMethod() ==
                PaymentMethod.COD) {

            fulfillOrder(savedOrder, cart);
        }

        return OrderMapper.toResponse(
                savedOrder
        );
    }

    /**
     * Completes an order after:
     *
     * 1. COD order creation
     * OR
     * 2. Successful Razorpay verification
     */
    @Override
    public OrderResponse confirmOrderAfterPayment(
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (order.getOrderStatus() ==
                OrderStatus.CONFIRMED) {

            return OrderMapper.toResponse(order);
        }

        if (order.getOrderStatus() ==
                OrderStatus.CANCELLED) {

            throw new BadRequestException(
                    "Cancelled order cannot be confirmed"
            );
        }

        Cart cart = cartRepository
                .findByUserUserId(
                        order.getUser()
                                .getUserId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        )
                );

        fulfillOrder(order, cart);

        return OrderMapper.toResponse(
                order
        );
    }

    /**
     * Completes the order:
     *
     * - Reduces stock
     * - Marks order CONFIRMED
     * - Clears cart
     */
    private void fulfillOrder(
            Order order,
            Cart cart
    ) {

        for (OrderItem orderItem :
                order.getOrderItems()) {

            ProductVariant variant =
                    orderItem.getVariant();

            if (variant.getStock() <
                    orderItem.getQuantity()) {

                throw new BadRequestException(
                        "Insufficient stock for "
                                + orderItem.getProductName()
                );
            }

            variant.setStock(
                    variant.getStock()
                            - orderItem.getQuantity()
            );

            if (variant.getStock() == 0) {
                variant.setAvailable(false);
            }
        }

        order.setOrderStatus(
                OrderStatus.CONFIRMED
        );

        /*
         * COD:
         * Payment stays PENDING because cash
         * has not been collected yet.
         *
         * Online:
         * PaymentService changes Payment/Order
         * payment status to SUCCESS before calling
         * this method.
         */

        cart.getCartItems().clear();

        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(
            Long userId,
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (!order.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Order does not belong to this user"
            );
        }

        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(
            Long userId
    ) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        return orderRepository
                .findByUserUserId(userId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(
            String status
    ) {

        OrderStatus orderStatus;

        try {

            orderStatus =
                    OrderStatus.valueOf(
                            status.toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new BadRequestException(
                    "Invalid order status: " + status
            );
        }

        return orderRepository
                .findByOrderStatus(orderStatus)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        order.setOrderStatus(
                request.getOrderStatus()
        );

        Order updatedOrder =
                orderRepository.save(order);

        return OrderMapper.toResponse(
                updatedOrder
        );
    }

    @Override
    public OrderResponse cancelOrder(
            Long userId,
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (!order.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Order does not belong to this user"
            );
        }

        if (order.getOrderStatus() ==
                OrderStatus.SHIPPED) {

            throw new BadRequestException(
                    "Shipped orders cannot be cancelled"
            );
        }

        if (order.getOrderStatus() ==
                OrderStatus.DELIVERED) {

            throw new BadRequestException(
                    "Delivered orders cannot be cancelled"
            );
        }

        if (order.getOrderStatus() ==
                OrderStatus.CANCELLED) {

            throw new BadRequestException(
                    "Order is already cancelled"
            );
        }

        order.setOrderStatus(
                OrderStatus.CANCELLED
        );

        Order cancelledOrder =
                orderRepository.save(order);

        return OrderMapper.toResponse(
                cancelledOrder
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersForAdmin() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByIdForAdmin(
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        return OrderMapper.toResponse(
                order
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatusForAdmin(
            String status
    ) {

        OrderStatus orderStatus;

        try {

            orderStatus =
                    OrderStatus.valueOf(
                            status.toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new BadRequestException(
                    "Invalid order status: " + status
            );
        }

        return orderRepository
                .findByOrderStatus(orderStatus)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}