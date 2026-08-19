package com.threadverse_backend.service.series;

import com.threadverse_backend.dto.request.CreateSeriesRequest;
import com.threadverse_backend.dto.request.UpdateSeriesRequest;
import com.threadverse_backend.dto.response.SeriesResponse;

import java.util.List;

public interface SeriesService {

    SeriesResponse createSeries(CreateSeriesRequest request);

    SeriesResponse updateSeries(Long seriesId, UpdateSeriesRequest request);

    void deleteSeries(Long seriesId);

    List<SeriesResponse> getAllSeries();

    SeriesResponse getSeriesById(Long seriesId);

    List<SeriesResponse> getSeriesByCollection(Long collectionId);
}