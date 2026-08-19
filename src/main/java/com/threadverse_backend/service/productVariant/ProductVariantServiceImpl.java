package com.threadverse_backend.service.productVariant;

import com.threadverse_backend.dto.request.CreateProductVariantRequest;
import com.threadverse_backend.dto.request.UpdateProductVariantRequest;
import com.threadverse_backend.dto.response.ProductVariantResponse;
import com.threadverse_backend.entity.Product;
import com.threadverse_backend.entity.ProductVariant;
import com.threadverse_backend.enums.Size;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.product.ProductVariantMapper;
import com.threadverse_backend.repository.productRepo.ProductRepository;
import com.threadverse_backend.repository.productVar.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    public ProductVariantResponse createVariant(
            CreateProductVariantRequest request) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        if (productVariantRepository
                .findByProductProductIdAndSize(
                        request.getProductId(),
                        request.getSize()
                )
                .isPresent()) {

            throw new BadRequestException(
                    "Variant with this size already exists for this product"
            );
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .size(request.getSize())
                .stock(request.getStock())
                .available(
                        request.getAvailable() != null
                                ? request.getAvailable()
                                : true
                )
                .build();

        ProductVariant savedVariant =
                productVariantRepository.save(variant);

        return productVariantMapper.toResponse(savedVariant);
    }

    @Override
    public ProductVariantResponse updateVariant(
            Long variantId,
            UpdateProductVariantRequest request) {

        ProductVariant variant =
                productVariantRepository.findById(variantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product variant not found"
                                )
                        );

        Size currentSize = variant.getSize();

        if (!currentSize.equals(request.getSize())) {

            Long productId =
                    variant.getProduct().getProductId();

            if (productVariantRepository
                    .findByProductProductIdAndSize(
                            productId,
                            request.getSize()
                    )
                    .isPresent()) {

                throw new BadRequestException(
                        "Variant with this size already exists for this product"
                );
            }
        }

        variant.setSize(request.getSize());
        variant.setStock(request.getStock());

        variant.setAvailable(
                request.getAvailable() != null
                        ? request.getAvailable()
                        : true
        );

        ProductVariant updatedVariant =
                productVariantRepository.save(variant);

        return productVariantMapper.toResponse(updatedVariant);
    }

    @Override
    public void deleteVariant(Long variantId) {

        ProductVariant variant =
                productVariantRepository.findById(variantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product variant not found"
                                )
                        );

        productVariantRepository.delete(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getVariantById(
            Long variantId) {

        ProductVariant variant =
                productVariantRepository.findById(variantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product variant not found"
                                )
                        );

        return productVariantMapper.toResponse(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsByProduct(
            Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        return productVariantRepository
                .findByProductProductId(productId)
                .stream()
                .map(productVariantMapper::toResponse)
                .toList();
    }
}