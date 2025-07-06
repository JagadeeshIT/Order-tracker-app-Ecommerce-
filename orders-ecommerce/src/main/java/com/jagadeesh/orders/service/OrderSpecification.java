package com.jagadeesh.orders.service;

import com.jagadeesh.orders.dto.OrderFilterRequest;
import com.jagadeesh.orders.model.Order;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> getOrdersByFilter(OrderFilterRequest filter) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getOrderId() != null && !filter.getOrderId().isEmpty()) {
                predicates.add(cb.equal(root.get("orderId"), filter.getOrderId()));
            }

            if (filter.getCustomerName() != null && !filter.getCustomerName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + filter.getCustomerName().toLowerCase() + "%"));
            }

            if (filter.getOrderItem() != null && !filter.getOrderItem().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("orderItem")), "%" + filter.getOrderItem().toLowerCase() + "%"));
            }

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deliveryDate"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deliveryDate"), filter.getEndDate()));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deliveryPrice"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deliveryPrice"), filter.getMaxPrice()));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("orderStatus")), filter.getStatus().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
