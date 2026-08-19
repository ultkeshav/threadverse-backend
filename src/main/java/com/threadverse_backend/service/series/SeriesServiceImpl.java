package com.threadverse_backend.service.series;

import com.threadverse_backend.dto.request.CreateSeriesRequest;
import com.threadverse_backend.dto.request.UpdateSeriesRequest;
import com.threadverse_backend.dto.response.SeriesResponse;
import com.threadverse_backend.entity.Collection;
import com.threadverse_backend.entity.Series;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.series.SeriesMapper;
import com.threadverse_backend.repository.collection.CollectionRepository;
import com.threadverse_backend.repository.series.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final CollectionRepository collectionRepository;
    private final SeriesMapper seriesMapper;

    @Override
    public SeriesResponse createSeries(
            CreateSeriesRequest request) {

        Collection collection = collectionRepository
                .findById(request.getCollectionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Collection not found"
                        )
                );

        Series series = Series.builder()
                .name(request.getName())
                .description(request.getDescription())
                .collection(collection)
                .build();

        Series savedSeries =
                seriesRepository.save(series);

        return seriesMapper.toResponse(savedSeries);
    }

    @Override
    public SeriesResponse updateSeries(
            Long seriesId,
            UpdateSeriesRequest request) {

        Series series = seriesRepository
                .findById(seriesId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Series not found"
                        )
                );

        Collection collection = collectionRepository
                .findById(request.getCollectionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Collection not found"
                        )
                );

        series.setName(request.getName());
        series.setDescription(request.getDescription());
        series.setCollection(collection);

        Series updatedSeries =
                seriesRepository.save(series);

        return seriesMapper.toResponse(updatedSeries);
    }

    @Override
    public void deleteSeries(Long seriesId) {

        Series series = seriesRepository
                .findById(seriesId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Series not found"
                        )
                );

        seriesRepository.delete(series);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> getAllSeries() {

        return seriesRepository.findAll()
                .stream()
                .map(seriesMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesResponse getSeriesById(
            Long seriesId) {

        Series series = seriesRepository
                .findById(seriesId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Series not found"
                        )
                );

        return seriesMapper.toResponse(series);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> getSeriesByCollection(
            Long collectionId) {

        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException(
                    "Collection not found"
            );
        }

        return seriesRepository
                .findByCollectionCollectionId(collectionId)
                .stream()
                .map(seriesMapper::toResponse)
                .toList();
    }
}