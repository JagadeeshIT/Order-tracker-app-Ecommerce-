package com.jagadeesh.orders.service;

import com.jagadeesh.orders.dto.OrderFilterRequest;
import com.jagadeesh.orders.model.Order;
import com.jagadeesh.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
    private OrderRepository orderRepository;
	

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase(status))
                .toList();
    }
    
    @Override
    public Page<Order> filterOrders(OrderFilterRequest filterRequest, Pageable pageable) {
        return orderRepository.findAll(OrderSpecification.getOrdersByFilter(filterRequest), pageable);
    }


}
