package com.threadverse_backend.service.productImage;

import com.threadverse_backend.dto.request.CreateProductImageRequest;
import com.threadverse_backend.dto.request.UpdateProductImageRequest;
import com.threadverse_backend.dto.response.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse createImage(
            CreateProductImageRequest request
    );

    ProductImageResponse updateImage(
            Long imageId,
            UpdateProductImageRequest request
    );

    void deleteImage(Long imageId);

    ProductImageResponse getImageById(Long imageId);

    List<ProductImageResponse> getImagesByProduct(Long productId);
}