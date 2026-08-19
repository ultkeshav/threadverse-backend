package com.threadverse_backend.controller.order;

import com.threadverse_backend.dto.request.CreateOrderRequest;
import com.threadverse_backend.dto.request.UpdateOrderStatusRequest;
import com.threadverse_backend.dto.response.OrderResponse;
import com.threadverse_backend.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestParam Long userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {

        return ResponseEntity.ok(
                orderService.createOrder(
                        userId,
                        request
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestParam Long userId,
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(
                        userId,
                        orderId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>>
    getUserOrders(
            @RequestParam Long userId
    ) {

        return ResponseEntity.ok(
                orderService.getUserOrders(userId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>>
    getOrdersByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                orderService.getOrdersByStatus(status)
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse>
    updateOrderStatus(
            @PathVariable Long orderId,
            @Valid
            @RequestBody UpdateOrderStatusRequest request
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request
                )
        );
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse>
    cancelOrder(
            @RequestParam Long userId,
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(
                        userId,
                        orderId
                )
        );
    }
}