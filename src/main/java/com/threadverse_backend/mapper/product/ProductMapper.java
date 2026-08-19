package com.threadverse_backend.mapper.product;

import com.threadverse_backend.dto.response.ProductResponse;
import com.threadverse_backend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {

        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .featured(product.getFeatured())
                .bestSeller(product.getBestSeller())
                .newArrival(product.getNewArrival())
                .active(product.getActive())
                .seriesId(
                        product.getSeries() != null
                                ? product.getSeries().getSeriesId()
                                : null
                )
                .seriesName(
                        product.getSeries() != null
                                ? product.getSeries().getName()
                                : null
                )
                .build();
    }
}