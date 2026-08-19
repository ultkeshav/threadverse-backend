package com.threadverse_backend.controller.product;

import com.threadverse_backend.dto.request.CreateProductRequest;
import com.threadverse_backend.dto.request.UpdateProductRequest;
import com.threadverse_backend.dto.response.ProductResponse;
import com.threadverse_backend.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/products")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse response =
                productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductResponse response =
                productService.updateProduct(productId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productService.getProductById(productId)
        );
    }

    @GetMapping("/series/{seriesId}/products")
    public ResponseEntity<List<ProductResponse>> getProductsBySeries(
            @PathVariable Long seriesId) {

        return ResponseEntity.ok(
                productService.getProductsBySeries(seriesId)
        );
    }

    @GetMapping("/products/featured")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts() {

        return ResponseEntity.ok(
                productService.getFeaturedProducts()
        );
    }

    @GetMapping("/products/best-sellers")
    public ResponseEntity<List<ProductResponse>> getBestSellerProducts() {

        return ResponseEntity.ok(
                productService.getBestSellerProducts()
        );
    }

    @GetMapping("/products/new-arrivals")
    public ResponseEntity<List<ProductResponse>> getNewArrivalProducts() {

        return ResponseEntity.ok(
                productService.getNewArrivalProducts()
        );
    }

    @GetMapping("/products/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {

        return ResponseEntity.ok(
                productService.getActiveProducts()
        );
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword)
        );
    }
}