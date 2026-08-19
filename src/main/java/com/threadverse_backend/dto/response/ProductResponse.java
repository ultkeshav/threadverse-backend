package com.threadverse_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long productId;

    private String name;

    private String slug;

    private String description;

    private BigDecimal price;

    private Boolean featured;

    private Boolean bestSeller;

    private Boolean newArrival;

    private Boolean active;

    private Long seriesId;

    private String seriesName;
}