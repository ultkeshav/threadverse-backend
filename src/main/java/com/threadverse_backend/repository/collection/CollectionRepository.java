package com.threadverse_backend.repository.collection;

import com.threadverse_backend.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Optional<Collection> findByName(String name);

    boolean existsByName(String name);
}