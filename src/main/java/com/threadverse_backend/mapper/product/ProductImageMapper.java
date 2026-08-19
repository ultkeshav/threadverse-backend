package com.threadverse_backend.mapper.product;

import com.threadverse_backend.dto.response.ProductImageResponse;
import com.threadverse_backend.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageResponse toResponse(ProductImage image) {

        if (image == null) {
            return null;
        }

        return ProductImageResponse.builder()
                .imageId(image.getImageId())
                .productId(
                        image.getProduct() != null
                                ? image.getProduct().getProductId()
                                : null
                )
                .imageUrl(image.getImageUrl())
                .displayOrder(image.getDisplayOrder())
                .build();
    }
}