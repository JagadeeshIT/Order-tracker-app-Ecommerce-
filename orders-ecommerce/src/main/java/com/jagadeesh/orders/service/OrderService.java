package com.jagadeesh.orders.service;

import com.jagadeesh.orders.dto.OrderFilterRequest;
import com.jagadeesh.orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(Long id);

    List<Order> getOrdersByStatus(String status);

    Page<Order> filterOrders(OrderFilterRequest filterRequest, Pageable pageable);
}
