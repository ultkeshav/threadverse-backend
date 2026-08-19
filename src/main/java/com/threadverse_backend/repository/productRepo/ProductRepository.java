package com.threadverse_backend.repository.productRepo;

import com.threadverse_backend.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "series")
    List<Product> findAll();

    @EntityGraph(attributePaths = "series")
    Optional<Product> findById(Long productId);

    Optional<Product> findBySlug(String slug);

    @EntityGraph(attributePaths = "series")
    List<Product> findBySeriesSeriesId(Long seriesId);

    List<Product> findByFeaturedTrue();

    List<Product> findByBestSellerTrue();

    List<Product> findByNewArrivalTrue();

    List<Product> findByActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String keyword);

    boolean existsBySlug(String slug);
}