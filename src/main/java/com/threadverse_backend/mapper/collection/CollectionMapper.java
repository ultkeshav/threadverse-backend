package com.threadverse_backend.mapper.collection;

import com.threadverse_backend.dto.request.CreateCollectionRequest;
import com.threadverse_backend.dto.request.UpdateCollectionRequest;
import com.threadverse_backend.dto.response.CollectionResponse;
import com.threadverse_backend.entity.Collection;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {

    public Collection toEntity(CreateCollectionRequest request) {

        return Collection.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(UpdateCollectionRequest request,
                             Collection collection) {

        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
    }

    public CollectionResponse toResponse(Collection collection) {

        return CollectionResponse.builder()
                .collectionId(collection.getCollectionId())
                .name(collection.getName())
                .description(collection.getDescription())
                .build();
    }
}