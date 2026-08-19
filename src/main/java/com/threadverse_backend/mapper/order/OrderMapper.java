package com.threadverse_backend.mapper.order;

import com.threadverse_backend.dto.response.OrderItemResponse;
import com.threadverse_backend.dto.response.OrderResponse;
import com.threadverse_backend.entity.Order;
import com.threadverse_backend.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(
            Order order
    ) {

        if (order == null) {
            return null;
        }

        List<OrderItemResponse> items =
                order.getOrderItems()
                        .stream()
                        .map(OrderMapper::mapItem)
                        .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderStatus(
                        order.getOrderStatus().name()
                )
                .paymentMethod(
                        order.getPaymentMethod().name()
                )
                .paymentStatus(
                        order.getPaymentStatus().name()
                )
                .totalAmount(
                        order.getTotalAmount()
                )
                .items(items)
                .build();
    }

    private static OrderItemResponse mapItem(
            OrderItem item
    ) {

        return OrderItemResponse.builder()
                .productName(
                        item.getProductName()
                )
                .size(
                        item.getProductSize().name()
                )
                .quantity(
                        item.getQuantity()
                )
                .unitPrice(
                        item.getUnitPrice()
                )
                .build();
    }
}