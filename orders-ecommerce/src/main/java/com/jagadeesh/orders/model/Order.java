package com.jagadeesh.orders.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId; // AKN12508, SRT32123, etc.

    @Column(nullable = false)
    private String customerName; // Olivera Nules, David Dorenli, etc.

    @Column(nullable = false)
    private String orderItem; // Adidas Hat, Puma Shorts, etc.

    @Column
    private LocalDate deliveryDate; // 12.04.2021

    @Column
    private Double deliveryPrice; // 24.90, 89.90 etc.

    @Column(nullable = false)
    private String deliveryStatus; // Complete, Continue, Canceled, Restitute

    @Column(nullable = false)
    private String orderStatus; // Same as deliveryStatus for tabs (e.g., Completed)

    // Constructors
    public Order() {
    }

    public Order(Long id, String orderId, String customerName, String orderItem, LocalDate deliveryDate,
                 Double deliveryPrice, String deliveryStatus, String orderStatus) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderItem = orderItem;
        this.deliveryDate = deliveryDate;
        this.deliveryPrice = deliveryPrice;
        this.deliveryStatus = deliveryStatus;
        this.orderStatus = orderStatus;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
