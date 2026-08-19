package com.threadverse_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCollectionRequest {

    @NotBlank(message = "Collection name is required")
    private String name;

    private String description;
}