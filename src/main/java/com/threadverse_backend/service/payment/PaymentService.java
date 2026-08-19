package com.threadverse_backend.service.payment;

import com.threadverse_backend.dto.request.VerifyPaymentRequest;
import com.threadverse_backend.dto.response.CreateRazorpayOrderResponse;
import com.threadverse_backend.dto.response.PaymentResponse;

public interface PaymentService {

    CreateRazorpayOrderResponse createRazorpayOrder(
            Long userId,
            Long orderId
    );

    PaymentResponse verifyPayment(
            Long userId,
            VerifyPaymentRequest request
    );

    PaymentResponse getPaymentByOrderId(
            Long userId,
            Long orderId
    );
}