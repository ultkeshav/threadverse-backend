package com.threadverse_backend.controller.product;

import com.threadverse_backend.dto.request.CreateProductImageRequest;
import com.threadverse_backend.dto.request.UpdateProductImageRequest;
import com.threadverse_backend.dto.response.ProductImageResponse;
import com.threadverse_backend.service.productImage.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/admin/product-images")
    public ResponseEntity<ProductImageResponse> createImage(
            @Valid @RequestBody CreateProductImageRequest request) {

        ProductImageResponse response =
                productImageService.createImage(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/admin/product-images/{imageId}")
    public ResponseEntity<ProductImageResponse> updateImage(
            @PathVariable Long imageId,
            @Valid @RequestBody UpdateProductImageRequest request) {

        ProductImageResponse response =
                productImageService.updateImage(
                        imageId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/product-images/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long imageId) {

        productImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product-images/{imageId}")
    public ResponseEntity<ProductImageResponse> getImageById(
            @PathVariable Long imageId) {

        return ResponseEntity.ok(
                productImageService.getImageById(imageId)
        );
    }

    @GetMapping("/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getImagesByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productImageService.getImagesByProduct(productId)
        );
    }
}