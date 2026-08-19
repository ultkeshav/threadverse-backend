package com.threadverse_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionResponse {

    private Long collectionId;

    private String name;

    private String description;
}