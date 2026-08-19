package com.threadverse_backend.service.collection;

import com.threadverse_backend.dto.request.CreateCollectionRequest;
import com.threadverse_backend.dto.request.UpdateCollectionRequest;
import com.threadverse_backend.dto.response.CollectionResponse;
import com.threadverse_backend.entity.Collection;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.collection.CollectionMapper;
import com.threadverse_backend.repository.collection.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;

    @Override
    public CollectionResponse createCollection(
            CreateCollectionRequest request) {

        Collection collection =
                collectionMapper.toEntity(request);

        Collection savedCollection =
                collectionRepository.save(collection);

        return collectionMapper.toResponse(savedCollection);
    }

    @Override
    public CollectionResponse updateCollection(
            Long collectionId,
            UpdateCollectionRequest request) {

        Collection collection =
                collectionRepository.findById(collectionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Collection not found"
                                )
                        );

        collectionMapper.updateEntity(
                request,
                collection
        );

        Collection updatedCollection =
                collectionRepository.save(collection);

        return collectionMapper.toResponse(
                updatedCollection
        );
    }

    @Override
    public void deleteCollection(Long collectionId) {

        Collection collection =
                collectionRepository.findById(collectionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Collection not found"
                                )
                        );

        collectionRepository.delete(collection);
    }

    @Override
    public CollectionResponse getCollectionById(
            Long collectionId) {

        Collection collection =
                collectionRepository.findById(collectionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Collection not found"
                                )
                        );

        return collectionMapper.toResponse(collection);
    }

    @Override
    public List<CollectionResponse> getAllCollections() {

        return collectionRepository.findAll()
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }
}