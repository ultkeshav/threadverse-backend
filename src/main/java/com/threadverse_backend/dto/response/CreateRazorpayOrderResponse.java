package com.threadverse_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRazorpayOrderResponse {

    private Long orderId;

    private Long paymentId;

    private String razorpayOrderId;

    private String razorpayKeyId;

    private BigDecimal amount;

    private Long amountInPaise;

    private String currency;

    private String customerName;

    private String customerEmail;

    private String customerPhone;
}