package com.threadverse_backend.repository.productVar;

import com.threadverse_backend.entity.ProductVariant;
import com.threadverse_backend.enums.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductProductId(Long productId);

    Optional<ProductVariant> findByProductProductIdAndSize(Long productId, Size size);
}