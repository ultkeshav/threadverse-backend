package com.threadverse_backend.mapper.series;

import com.threadverse_backend.dto.response.SeriesResponse;
import com.threadverse_backend.entity.Series;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {

    public SeriesResponse toResponse(Series series) {

        return SeriesResponse.builder()
                .seriesId(series.getSeriesId())
                .name(series.getName())
                .description(series.getDescription())
                .collectionId(series.getCollection().getCollectionId())
                .collectionName(series.getCollection().getName())
                .build();
    }
}