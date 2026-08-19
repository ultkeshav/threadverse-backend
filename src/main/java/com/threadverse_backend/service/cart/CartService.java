package com.threadverse_backend.service.cart;

import com.threadverse_backend.dto.request.AddCartItemRequest;
import com.threadverse_backend.dto.request.UpdateCartItemRequest;
import com.threadverse_backend.dto.response.CartResponse;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItem(
            Long userId,
            AddCartItemRequest request
    );

    CartResponse updateItem(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequest request
    );

    void removeItem(
            Long userId,
            Long cartItemId
    );
}