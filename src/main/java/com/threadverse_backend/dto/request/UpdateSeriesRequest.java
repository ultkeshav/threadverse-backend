package com.threadverse_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSeriesRequest {

    @NotBlank(message = "Series name is required")
    private String name;

    private String description;

    @NotNull(message = "Collection ID is required")
    private Long collectionId;
}