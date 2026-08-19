package com.threadverse_backend.controller.product;

import com.threadverse_backend.dto.request.CreateProductVariantRequest;
import com.threadverse_backend.dto.request.UpdateProductVariantRequest;
import com.threadverse_backend.dto.response.ProductVariantResponse;
import com.threadverse_backend.service.productVariant.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping("/admin/product-variants")
    public ResponseEntity<ProductVariantResponse> createVariant(
            @Valid @RequestBody CreateProductVariantRequest request) {

        ProductVariantResponse response =
                productVariantService.createVariant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/admin/product-variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody UpdateProductVariantRequest request) {

        ProductVariantResponse response =
                productVariantService.updateVariant(
                        variantId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/product-variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(
            @PathVariable Long variantId) {

        productVariantService.deleteVariant(variantId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product-variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> getVariantById(
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                productVariantService.getVariantById(variantId)
        );
    }

    @GetMapping("/products/{productId}/variants")
    public ResponseEntity<List<ProductVariantResponse>> getVariantsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productVariantService.getVariantsByProduct(productId)
        );
    }
}