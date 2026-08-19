package com.threadverse_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductImageRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder = 0;

    @NotNull(message = "Product ID is required")
    private Long productId;
}