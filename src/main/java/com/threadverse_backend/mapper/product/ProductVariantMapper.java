package com.threadverse_backend.mapper.product;

import com.threadverse_backend.dto.response.ProductVariantResponse;
import com.threadverse_backend.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariantResponse toResponse(ProductVariant variant) {

        if (variant == null) {
            return null;
        }

        return ProductVariantResponse.builder()
                .variantId(variant.getVariantId())
                .productId(
                        variant.getProduct() != null
                                ? variant.getProduct().getProductId()
                                : null
                )
                .size(variant.getSize())
                .stock(variant.getStock())
                .available(variant.getAvailable())
                .build();
    }
}