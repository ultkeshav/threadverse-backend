package com.threadverse_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private String orderStatus;

    private String paymentMethod;

    private String paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderItemResponse> items;
}