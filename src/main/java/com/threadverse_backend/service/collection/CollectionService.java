package com.threadverse_backend.service.collection;

import com.threadverse_backend.dto.request.CreateCollectionRequest;
import com.threadverse_backend.dto.request.UpdateCollectionRequest;
import com.threadverse_backend.dto.response.CollectionResponse;

import java.util.List;

public interface CollectionService {

    CollectionResponse createCollection(CreateCollectionRequest request);

    CollectionResponse updateCollection(Long collectionId,
                                        UpdateCollectionRequest request);

    void deleteCollection(Long collectionId);

    CollectionResponse getCollectionById(Long collectionId);

    List<CollectionResponse> getAllCollections();
}