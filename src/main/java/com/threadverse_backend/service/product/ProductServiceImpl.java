package com.threadverse_backend.service.product;

import com.threadverse_backend.dto.request.CreateProductRequest;
import com.threadverse_backend.dto.request.UpdateProductRequest;
import com.threadverse_backend.dto.response.ProductResponse;
import com.threadverse_backend.entity.Product;
import com.threadverse_backend.entity.Series;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.product.ProductMapper;
import com.threadverse_backend.repository.productRepo.ProductRepository;
import com.threadverse_backend.repository.series.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SeriesRepository seriesRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(
            CreateProductRequest request) {

        if (productRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException(
                    "Product slug already exists"
            );
        }

        Series series = seriesRepository
                .findById(request.getSeriesId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Series not found"
                        )
                );

        Product product = Product.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .price(request.getPrice())
                .featured(
                        request.getFeatured() != null
                                ? request.getFeatured()
                                : false
                )
                .bestSeller(
                        request.getBestSeller() != null
                                ? request.getBestSeller()
                                : false
                )
                .newArrival(
                        request.getNewArrival() != null
                                ? request.getNewArrival()
                                : false
                )
                .active(
                        request.getActive() != null
                                ? request.getActive()
                                : true
                )
                .series(series)
                .build();

        Product savedProduct =
                productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(
            Long productId,
            UpdateProductRequest request) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                )
                        );

        if (!product.getSlug().equals(request.getSlug())
                && productRepository.existsBySlug(request.getSlug())) {

            throw new BadRequestException(
                    "Product slug already exists"
            );
        }

        Series series = seriesRepository
                .findById(request.getSeriesId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Series not found"
                        )
                );

        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setFeatured(
                request.getFeatured() != null
                        ? request.getFeatured()
                        : false
        );
        product.setBestSeller(
                request.getBestSeller() != null
                        ? request.getBestSeller()
                        : false
        );
        product.setNewArrival(
                request.getNewArrival() != null
                        ? request.getNewArrival()
                        : false
        );
        product.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );
        product.setSeries(series);

        Product updatedProduct =
                productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                )
                        );

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(
            Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                )
                        );

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsBySeries(
            Long seriesId) {

        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException(
                    "Series not found"
            );
        }

        return productRepository
                .findBySeriesSeriesId(seriesId)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {

        return productRepository.findByFeaturedTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getBestSellerProducts() {

        return productRepository.findByBestSellerTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getNewArrivalProducts() {

        return productRepository.findByNewArrivalTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(
            String keyword) {

        return productRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }
}