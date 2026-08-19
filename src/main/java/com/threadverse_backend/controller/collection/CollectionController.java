package com.threadverse_backend.controller.collection;

import com.threadverse_backend.dto.request.CreateCollectionRequest;
import com.threadverse_backend.dto.request.UpdateCollectionRequest;
import com.threadverse_backend.dto.response.CollectionResponse;
import com.threadverse_backend.service.collection.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/admin/collections")
    public ResponseEntity<CollectionResponse> createCollection(
            @Valid @RequestBody CreateCollectionRequest request) {

        CollectionResponse response = collectionService.createCollection(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/admin/collections/{collectionId}")
    public ResponseEntity<CollectionResponse> updateCollection(
            @PathVariable Long collectionId,
            @Valid @RequestBody UpdateCollectionRequest request) {

        CollectionResponse response =
                collectionService.updateCollection(collectionId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/collections/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long collectionId) {

        collectionService.deleteCollection(collectionId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collections")
    public ResponseEntity<List<CollectionResponse>> getAllCollections() {

        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<CollectionResponse> getCollectionById(
            @PathVariable Long collectionId) {

        return ResponseEntity.ok(
                collectionService.getCollectionById(collectionId)
        );
    }
}