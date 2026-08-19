package com.threadverse_backend.mapper.cart;

import com.threadverse_backend.dto.response.CartItemResponse;
import com.threadverse_backend.dto.response.CartResponse;
import com.threadverse_backend.entity.Cart;
import com.threadverse_backend.entity.CartItem;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    private CartMapper() {
    }

    public static CartResponse toResponse(Cart cart) {

        if (cart == null) {
            return null;
        }

        List<CartItemResponse> items =
                cart.getCartItems() == null
                        ? new ArrayList<>()
                        : cart.getCartItems()
                        .stream()
                        .map(CartMapper::mapItem)
                        .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .items(items)
                .totalAmount(total)
                .build();
    }

    private static CartItemResponse mapItem(CartItem item) {

        BigDecimal price = item.getVariant().getProduct().getPrice();

        return CartItemResponse.builder()
                .cartItemId(item.getCartItemId())
                .variantId(item.getVariant().getVariantId())
                .productName(item.getVariant().getProduct().getName())
                .size(item.getVariant().getSize().name())
                .quantity(item.getQuantity())
                .unitPrice(price)
                .totalPrice(price.multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}