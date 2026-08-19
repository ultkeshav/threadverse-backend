package com.threadverse_backend.repository.order;

import com.threadverse_backend.entity.Order;
import com.threadverse_backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserUserId(
            Long userId
    );

    List<Order> findByOrderStatus(
            OrderStatus orderStatus
    );
}