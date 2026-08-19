package com.threadverse_backend.repository.payment;

import com.threadverse_backend.entity.Payment;
import com.threadverse_backend.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderOrderId(
            Long orderId
    );

    List<Payment> findByPaymentStatus(
            PaymentStatus paymentStatus
    );
}