package com.threadverse_backend.dto.response;

import com.threadverse_backend.enums.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {

    private Long variantId;

    private Long productId;

    private Size size;

    private Integer stock;

    private Boolean available;
}