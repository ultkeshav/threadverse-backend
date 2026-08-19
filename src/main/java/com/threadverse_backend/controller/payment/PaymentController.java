package com.threadverse_backend.controller.payment;

import com.threadverse_backend.dto.request.VerifyPaymentRequest;
import com.threadverse_backend.dto.response.CreateRazorpayOrderResponse;
import com.threadverse_backend.dto.response.PaymentResponse;
import com.threadverse_backend.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/razorpay/order/{orderId}")
    public ResponseEntity<CreateRazorpayOrderResponse>
    createRazorpayOrder(
            @RequestParam Long userId,
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                paymentService.createRazorpayOrder(
                        userId,
                        orderId
                )
        );
    }

    @PostMapping("/razorpay/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(
            @RequestParam Long userId,
            @Valid @RequestBody VerifyPaymentRequest request
    ) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(
                        userId,
                        request
                )
        );
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponse>
    getPaymentByOrderId(
            @RequestParam Long userId,
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(
                        userId,
                        orderId
                )
        );
    }
}