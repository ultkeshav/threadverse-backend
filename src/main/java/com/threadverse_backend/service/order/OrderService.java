package com.threadverse_backend.service.order;

import com.threadverse_backend.dto.request.CreateOrderRequest;
import com.threadverse_backend.dto.request.UpdateOrderStatusRequest;
import com.threadverse_backend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(
            Long userId,
            CreateOrderRequest request
    );

    OrderResponse getOrderById(
            Long userId,
            Long orderId
    );

    List<OrderResponse> getUserOrders(
            Long userId
    );

    List<OrderResponse> getOrdersByStatus(
            String status
    );

    OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    OrderResponse cancelOrder(
            Long userId,
            Long orderId
    );

    List<OrderResponse> getAllOrdersForAdmin();

    OrderResponse getOrderByIdForAdmin(
            Long orderId
    );

    List<OrderResponse> getOrdersByStatusForAdmin(
            String status
    );

    OrderResponse confirmOrderAfterPayment(
            Long orderId
    );
}