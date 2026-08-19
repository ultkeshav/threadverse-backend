package com.threadverse_backend.service.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.threadverse_backend.dto.request.VerifyPaymentRequest;
import com.threadverse_backend.dto.response.CreateRazorpayOrderResponse;
import com.threadverse_backend.dto.response.PaymentResponse;
import com.threadverse_backend.entity.Payment;
import com.threadverse_backend.entity.User;
import com.threadverse_backend.enums.PaymentMethod;
import com.threadverse_backend.enums.PaymentStatus;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.repository.order.OrderRepository;
import com.threadverse_backend.repository.payment.PaymentRepository;
import com.threadverse_backend.repository.user.UserRepository;
import com.threadverse_backend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    @Override
    public CreateRazorpayOrderResponse createRazorpayOrder(
            Long userId,
            Long orderId
    ) {

        System.out.println(
                "========== RAZORPAY CREATE ORDER =========="
        );

        System.out.println(
                "User ID: " + userId
        );

        System.out.println(
                "ThreadVerse Order ID: " + orderId
        );

        /*
         * Never print the Razorpay secret.
         * Printing the public Key ID is okay for debugging.
         */
        System.out.println(
                "Razorpay Key ID: " + razorpayKeyId
        );

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        com.threadverse_backend.entity.Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        /*
         * Security:
         * A user can only create a payment for
         * their own order.
         */
        if (!order.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Order does not belong to this user"
            );
        }

        System.out.println(
                "Payment Method: "
                        + order.getPaymentMethod()
        );

        System.out.println(
                "Order Status: "
                        + order.getOrderStatus()
        );

        System.out.println(
                "Payment Status: "
                        + order.getPaymentStatus()
        );

        /*
         * COD doesn't need Razorpay.
         */
        if (order.getPaymentMethod() ==
                PaymentMethod.COD) {

            throw new BadRequestException(
                    "Razorpay is not required for COD"
            );
        }

        /*
         * The payment record was created when
         * the ThreadVerse order was created.
         */
        Payment payment =
                paymentRepository
                        .findByOrderOrderId(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        System.out.println(
                "Payment ID: "
                        + payment.getPaymentId()
        );

        System.out.println(
                "Payment Amount: "
                        + payment.getAmount()
        );

        System.out.println(
                "Payment Status: "
                        + payment.getPaymentStatus()
        );

        /*
         * Don't create another Razorpay order
         * for an already successful payment.
         */
        if (payment.getPaymentStatus() ==
                PaymentStatus.SUCCESS) {

            throw new BadRequestException(
                    "Payment is already completed"
            );
        }

        /*
         * If the user retries the same pending
         * payment, reuse the existing Razorpay
         * order instead of creating another one.
         */
        if (payment.getRazorpayOrderId() != null &&
                !payment.getRazorpayOrderId()
                        .isBlank()) {

            System.out.println(
                    "Existing Razorpay Order ID found: "
                            + payment.getRazorpayOrderId()
            );

            return buildRazorpayOrderResponse(
                    user,
                    payment,
                    payment.getRazorpayOrderId()
            );
        }

        try {

            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            razorpayKeyId,
                            razorpayKeySecret
                    );

            /*
             * Razorpay expects the amount in the
             * smallest currency unit.
             *
             * ₹799.00 -> 79900 paise
             */
            long amountInPaise =
                    payment.getAmount()
                            .movePointRight(2)
                            .setScale(
                                    0,
                                    RoundingMode.UNNECESSARY
                            )
                            .longValueExact();

            System.out.println(
                    "Amount in paise: "
                            + amountInPaise
            );

            JSONObject orderRequest =
                    new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            /*
             * Receipt must be unique.
             */
            orderRequest.put(
                    "receipt",
                    "threadverse_" + orderId
            );

            /*
             * Useful for identifying the
             * ThreadVerse order inside Razorpay.
             */
            JSONObject notes =
                    new JSONObject();

            notes.put(
                    "threadverse_order_id",
                    String.valueOf(orderId)
            );

            notes.put(
                    "threadverse_user_id",
                    String.valueOf(userId)
            );

            orderRequest.put(
                    "notes",
                    notes
            );

            System.out.println(
                    "Sending order creation request to Razorpay..."
            );

            Order razorpayOrder =
                    razorpayClient.orders.create(
                            orderRequest
                    );

            String razorpayOrderId =
                    razorpayOrder.get("id");

            System.out.println(
                    "Razorpay order created successfully."
            );

            System.out.println(
                    "Razorpay Order ID: "
                            + razorpayOrderId
            );

            /*
             * Save Razorpay order ID against
             * our Payment record.
             */
            payment.setRazorpayOrderId(
                    razorpayOrderId
            );

            paymentRepository.save(payment);

            return buildRazorpayOrderResponse(
                    user,
                    payment,
                    razorpayOrderId
            );

        } catch (RazorpayException e) {

            System.out.println(
                    "========== RAZORPAY CREATE FAILED =========="
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            throw new BadRequestException(
                    "Unable to create Razorpay order: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "========== UNEXPECTED PAYMENT ERROR =========="
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            throw new BadRequestException(
                    "Unable to create Razorpay order"
            );
        }
    }

    @Override
    public PaymentResponse verifyPayment(
            Long userId,
            VerifyPaymentRequest request
    ) {

        System.out.println(
                "========== RAZORPAY VERIFY =========="
        );

        System.out.println(
                "User ID: " + userId
        );

        System.out.println(
                "ThreadVerse Order ID: "
                        + request.getOrderId()
        );

        System.out.println(
                "Razorpay Payment ID: "
                        + request.getRazorpayPaymentId()
        );

        System.out.println(
                "Razorpay Order ID from frontend: "
                        + request.getRazorpayOrderId()
        );

        /*
         * Load and validate user.
         */
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        /*
         * Load our order.
         */
        com.threadverse_backend.entity.Order order =
                orderRepository
                        .findById(
                                request.getOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        /*
         * Security:
         * Make sure this order belongs to
         * the authenticated user.
         */
        if (!order.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Order does not belong to this user"
            );
        }

        /*
         * Load payment.
         */
        Payment payment =
                paymentRepository
                        .findByOrderOrderId(
                                request.getOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        System.out.println(
                "Database Razorpay Order ID: "
                        + payment.getRazorpayOrderId()
        );

        System.out.println(
                "Current Payment Status: "
                        + payment.getPaymentStatus()
        );

        /*
         * Idempotency:
         * If this payment is already marked
         * SUCCESS, don't process it again.
         */
        if (payment.getPaymentStatus() ==
                PaymentStatus.SUCCESS) {

            System.out.println(
                    "Payment already marked SUCCESS."
            );

            return toResponse(payment);
        }

        /*
         * A Razorpay order must have been created
         * before verification.
         */
        if (payment.getRazorpayOrderId() == null ||
                payment.getRazorpayOrderId()
                        .isBlank()) {

            throw new BadRequestException(
                    "Razorpay order has not been created"
            );
        }

        /*
         * Never trust the browser's Razorpay
         * order ID blindly.
         *
         * Compare it against the value we saved
         * when we created the Razorpay order.
         */
        if (!payment.getRazorpayOrderId()
                .equals(
                        request.getRazorpayOrderId()
                )) {

            throw new BadRequestException(
                    "Razorpay order ID mismatch"
            );
        }

        try {

            JSONObject verificationData =
                    new JSONObject();

            verificationData.put(
                    "razorpay_order_id",
                    payment.getRazorpayOrderId()
            );

            verificationData.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            verificationData.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            System.out.println(
                    "Verifying Razorpay signature..."
            );

            /*
             * Razorpay's official Java SDK performs
             * the HMAC SHA-256 signature validation.
             */
            boolean verified =
                    Utils.verifyPaymentSignature(
                            verificationData,
                            razorpayKeySecret
                    );

            System.out.println(
                    "Signature verified: "
                            + verified
            );

            if (!verified) {

                payment.setPaymentStatus(
                        PaymentStatus.FAILED
                );

                paymentRepository.save(payment);

                order.setPaymentStatus(
                        PaymentStatus.FAILED
                );

                orderRepository.save(order);

                throw new BadRequestException(
                        "Payment signature verification failed"
                );
            }

            /*
             * Store the successful Razorpay
             * payment information.
             */
            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            payment.setPaymentStatus(
                    PaymentStatus.SUCCESS
            );

            paymentRepository.save(payment);

            System.out.println(
                    "Payment status changed to SUCCESS."
            );

            /*
             * Keep the Order's payment status
             * synchronized.
             */
            order.setPaymentStatus(
                    PaymentStatus.SUCCESS
            );

            orderRepository.save(order);

            /*
             * Now — and only now — fulfil the order.
             *
             * This method should:
             * - confirm the order
             * - reduce stock
             * - remove only purchased cart items
             */
            orderService.confirmOrderAfterPayment(
                    order.getOrderId()
            );

            System.out.println(
                    "Order confirmed successfully: "
                            + order.getOrderId()
            );

            return toResponse(payment);

        } catch (BadRequestException e) {

            throw e;

        } catch (Exception e) {

            System.out.println(
                    "========== RAZORPAY VERIFY FAILED =========="
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            /*
             * If verification fails unexpectedly,
             * don't treat the payment as successful.
             */
            payment.setPaymentStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(payment);

            order.setPaymentStatus(
                    PaymentStatus.FAILED
            );

            orderRepository.save(order);

            throw new BadRequestException(
                    "Unable to verify payment: "
                            + e.getMessage()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long userId,
            Long orderId
    ) {

        System.out.println(
                "========== GET PAYMENT =========="
        );

        System.out.println(
                "User ID: " + userId
        );

        System.out.println(
                "Order ID: " + orderId
        );

        com.threadverse_backend.entity.Order order =
                orderRepository
                        .findById(orderId)
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

        Payment payment =
                paymentRepository
                        .findByOrderOrderId(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        return toResponse(payment);
    }

    private CreateRazorpayOrderResponse
    buildRazorpayOrderResponse(
            User user,
            Payment payment,
            String razorpayOrderId
    ) {

        long amountInPaise =
                payment.getAmount()
                        .movePointRight(2)
                        .setScale(
                                0,
                                RoundingMode.UNNECESSARY
                        )
                        .longValueExact();

        return CreateRazorpayOrderResponse
                .builder()
                .orderId(
                        payment.getOrder()
                                .getOrderId()
                )
                .paymentId(
                        payment.getPaymentId()
                )
                .razorpayOrderId(
                        razorpayOrderId
                )
                .razorpayKeyId(
                        razorpayKeyId
                )
                .amount(
                        payment.getAmount()
                )
                .amountInPaise(
                        amountInPaise
                )
                .currency("INR")
                .customerName(
                        buildCustomerName(user)
                )
                .customerEmail(
                        user.getEmail()
                )
                .customerPhone(
                        user.getPhone()
                )
                .build();
    }

    private String buildCustomerName(
            User user
    ) {

        String firstName =
                user.getFirstName() == null
                        ? ""
                        : user.getFirstName().trim();

        String lastName =
                user.getLastName() == null
                        ? ""
                        : user.getLastName().trim();

        return (firstName + " " + lastName)
                .trim();
    }

    private PaymentResponse toResponse(
            Payment payment
    ) {

        return PaymentResponse.builder()
                .orderId(
                        payment.getOrder()
                                .getOrderId()
                )
                .paymentId(
                        payment.getPaymentId()
                )
                .paymentMethod(
                        payment.getPaymentMethod()
                                .name()
                )
                .paymentStatus(
                        payment.getPaymentStatus()
                                .name()
                )
                .amount(
                        payment.getAmount()
                )
                .razorpayOrderId(
                        payment.getRazorpayOrderId()
                )
                .razorpayPaymentId(
                        payment.getRazorpayPaymentId()
                )
                .razorpayKeyId(
                        razorpayKeyId
                )
                .currency("INR")
                .build();
    }
}