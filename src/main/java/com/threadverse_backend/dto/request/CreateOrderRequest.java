package com.threadverse_backend.dto.request;

import com.threadverse_backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull(message = "Address Id is required")
    private Long addressId;

    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;
}