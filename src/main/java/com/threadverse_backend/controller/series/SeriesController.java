package com.threadverse_backend.controller.series;

import com.threadverse_backend.dto.request.CreateSeriesRequest;
import com.threadverse_backend.dto.request.UpdateSeriesRequest;
import com.threadverse_backend.dto.response.SeriesResponse;
import com.threadverse_backend.service.series.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping("/admin/series")
    public ResponseEntity<SeriesResponse> createSeries(
            @Valid @RequestBody CreateSeriesRequest request) {

        SeriesResponse response =
                seriesService.createSeries(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/admin/series/{seriesId}")
    public ResponseEntity<SeriesResponse> updateSeries(
            @PathVariable Long seriesId,
            @Valid @RequestBody UpdateSeriesRequest request) {

        SeriesResponse response =
                seriesService.updateSeries(seriesId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/series/{seriesId}")
    public ResponseEntity<Void> deleteSeries(
            @PathVariable Long seriesId) {

        seriesService.deleteSeries(seriesId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/series")
    public ResponseEntity<List<SeriesResponse>> getAllSeries() {

        return ResponseEntity.ok(
                seriesService.getAllSeries()
        );
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<SeriesResponse> getSeriesById(
            @PathVariable Long seriesId) {

        return ResponseEntity.ok(
                seriesService.getSeriesById(seriesId)
        );
    }

    @GetMapping("/collections/{collectionId}/series")
    public ResponseEntity<List<SeriesResponse>> getSeriesByCollection(
            @PathVariable Long collectionId) {

        return ResponseEntity.ok(
                seriesService.getSeriesByCollection(collectionId)
        );
    }
}