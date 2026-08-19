package com.threadverse_backend.service.cart;

import com.threadverse_backend.dto.request.AddCartItemRequest;
import com.threadverse_backend.dto.request.UpdateCartItemRequest;
import com.threadverse_backend.dto.response.CartResponse;
import com.threadverse_backend.entity.Cart;
import com.threadverse_backend.entity.CartItem;
import com.threadverse_backend.entity.ProductVariant;
import com.threadverse_backend.entity.User;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.cart.CartMapper;
import com.threadverse_backend.repository.cart.CartRepository;
import com.threadverse_backend.repository.cartItem.CartItemRepository;
import com.threadverse_backend.repository.productVar.ProductVariantRepository;
import com.threadverse_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    /**
     * Gets the user's cart.
     *
     * If the user does not have a cart yet,
     * an empty cart is created automatically.
     */
    @Override
    public CartResponse getCart(Long userId) {

        Cart cart = getOrCreateCart(userId);

        return CartMapper.toResponse(cart);
    }

    /**
     * Adds a product variant to the user's cart.
     *
     * If the same variant already exists,
     * its quantity is increased instead of
     * creating another cart item.
     */
    @Override
    public CartResponse addItem(
            Long userId,
            AddCartItemRequest request
    ) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        ProductVariant variant =
                productVariantRepository
                        .findById(
                                request.getVariantId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product variant not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                variant.getAvailable()
        )) {

            throw new BadRequestException(
                    "This product variant is not available"
            );
        }

        if (request.getQuantity() > variant.getStock()) {

            throw new BadRequestException(
                    "Insufficient stock"
            );
        }

        /*
         * Create the cart automatically for a
         * newly registered user.
         */
        Cart cart = getOrCreateCart(user);

        CartItem existingItem =
                cartItemRepository
                        .findByCartCartIdAndVariantVariantId(
                                cart.getCartId(),
                                variant.getVariantId()
                        )
                        .orElse(null);

        if (existingItem != null) {

            int newQuantity =
                    existingItem.getQuantity()
                            + request.getQuantity();

            if (newQuantity > variant.getStock()) {

                throw new BadRequestException(
                        "Insufficient stock"
                );
            }

            existingItem.setQuantity(
                    newQuantity
            );

            cartItemRepository.save(
                    existingItem
            );

        } else {

            CartItem cartItem =
                    CartItem.builder()
                            .cart(cart)
                            .variant(variant)
                            .quantity(
                                    request.getQuantity()
                            )
                            .build();

            cartItemRepository.save(
                    cartItem
            );
        }

        return CartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateItem(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequest request
    ) {

        Cart cart = getOrCreateCart(userId);

        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"
                                )
                        );

        if (!cartItem.getCart()
                .getCartId()
                .equals(cart.getCartId())) {

            throw new BadRequestException(
                    "Cart item does not belong to this cart"
            );
        }

        ProductVariant variant =
                cartItem.getVariant();

        if (!Boolean.TRUE.equals(
                variant.getAvailable()
        )) {

            throw new BadRequestException(
                    "This product variant is not available"
            );
        }

        if (request.getQuantity() >
                variant.getStock()) {

            throw new BadRequestException(
                    "Insufficient stock"
            );
        }

        cartItem.setQuantity(
                request.getQuantity()
        );

        cartItemRepository.save(
                cartItem
        );

        return CartMapper.toResponse(cart);
    }

    @Override
    public void removeItem(
            Long userId,
            Long cartItemId
    ) {

        Cart cart = getOrCreateCart(userId);

        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"
                                )
                        );

        if (!cartItem.getCart()
                .getCartId()
                .equals(cart.getCartId())) {

            throw new BadRequestException(
                    "Cart item does not belong to this cart"
            );
        }

        cartItemRepository.delete(
                cartItem
        );
    }

    /**
     * Finds the user's cart or creates
     * an empty one when necessary.
     */
    private Cart getOrCreateCart(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return getOrCreateCart(user);
    }

    /**
     * Finds the user's cart or creates
     * an empty one when necessary.
     */
    private Cart getOrCreateCart(User user) {

        return cartRepository
                .findByUserUserId(
                        user.getUserId()
                )
                .orElseGet(() ->
                        cartRepository.save(
                                Cart.builder()
                                        .user(user)
                                        .build()
                        )
                );
    }
}