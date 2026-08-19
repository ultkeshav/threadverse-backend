package com.threadverse_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductImageRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder = 0;
}