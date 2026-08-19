package com.threadverse_backend.dto.request;

import com.threadverse_backend.enums.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductVariantRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Size is required")
    private Size size;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private Boolean available = true;
}