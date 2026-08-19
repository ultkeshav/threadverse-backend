package com.threadverse_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesResponse {

    private Long seriesId;

    private String name;

    private String description;

    private Long collectionId;

    private String collectionName;
}