package com.threadverse_backend.service.productImage;

import com.threadverse_backend.dto.request.CreateProductImageRequest;
import com.threadverse_backend.dto.request.UpdateProductImageRequest;
import com.threadverse_backend.dto.response.ProductImageResponse;
import com.threadverse_backend.entity.Product;
import com.threadverse_backend.entity.ProductImage;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.product.ProductImageMapper;
import com.threadverse_backend.repository.productImg.ProductImageRepository;
import com.threadverse_backend.repository.productRepo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    public ProductImageResponse createImage(
            CreateProductImageRequest request) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.getImageUrl())
                .displayOrder(
                        request.getDisplayOrder() != null
                                ? request.getDisplayOrder()
                                : 0
                )
                .build();

        ProductImage savedImage =
                productImageRepository.save(image);

        return productImageMapper.toResponse(savedImage);
    }

    @Override
    public ProductImageResponse updateImage(
            Long imageId,
            UpdateProductImageRequest request) {

        ProductImage image =
                productImageRepository.findById(imageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product image not found"
                                )
                        );

        image.setImageUrl(request.getImageUrl());

        image.setDisplayOrder(
                request.getDisplayOrder() != null
                        ? request.getDisplayOrder()
                        : 0
        );

        ProductImage updatedImage =
                productImageRepository.save(image);

        return productImageMapper.toResponse(updatedImage);
    }

    @Override
    public void deleteImage(Long imageId) {

        ProductImage image =
                productImageRepository.findById(imageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product image not found"
                                )
                        );

        productImageRepository.delete(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponse getImageById(
            Long imageId) {

        ProductImage image =
                productImageRepository.findById(imageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product image not found"
                                )
                        );

        return productImageMapper.toResponse(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImagesByProduct(
            Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        return productImageRepository
                .findByProductProductIdOrderByDisplayOrderAsc(productId)
                .stream()
                .map(productImageMapper::toResponse)
                .toList();
    }
}