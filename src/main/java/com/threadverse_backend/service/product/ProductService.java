package com.threadverse_backend.service.product;

import com.threadverse_backend.dto.request.CreateProductRequest;
import com.threadverse_backend.dto.request.UpdateProductRequest;
import com.threadverse_backend.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(
            Long productId,
            UpdateProductRequest request
    );

    void deleteProduct(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long productId);

    List<ProductResponse> getProductsBySeries(Long seriesId);

    List<ProductResponse> getFeaturedProducts();

    List<ProductResponse> getBestSellerProducts();

    List<ProductResponse> getNewArrivalProducts();

    List<ProductResponse> getActiveProducts();

    List<ProductResponse> searchProducts(String keyword);
}