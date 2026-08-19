package com.threadverse_backend.service.productVariant;

import com.threadverse_backend.dto.request.CreateProductVariantRequest;
import com.threadverse_backend.dto.request.UpdateProductVariantRequest;
import com.threadverse_backend.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponse createVariant(
            CreateProductVariantRequest request
    );

    ProductVariantResponse updateVariant(
            Long variantId,
            UpdateProductVariantRequest request
    );

    void deleteVariant(Long variantId);

    ProductVariantResponse getVariantById(Long variantId);

    List<ProductVariantResponse> getVariantsByProduct(Long productId);
}