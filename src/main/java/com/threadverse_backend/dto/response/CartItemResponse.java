package com.threadverse_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long cartItemId;

    private Long variantId;

    private String productName;

    private String size;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}