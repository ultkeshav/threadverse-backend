package com.threadverse_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {

    private Long imageId;

    private Long productId;

    private String imageUrl;

    private Integer displayOrder;
}