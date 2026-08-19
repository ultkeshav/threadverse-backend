package com.threadverse_backend.repository.series;

import com.threadverse_backend.entity.Series;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @EntityGraph(attributePaths = "collection")
    List<Series> findAll();

    @EntityGraph(attributePaths = "collection")
    Optional<Series> findById(Long seriesId);

    @EntityGraph(attributePaths = "collection")
    List<Series> findByCollectionCollectionId(Long collectionId);

    Optional<Series> findByName(String name);

    boolean existsByName(String name);
}