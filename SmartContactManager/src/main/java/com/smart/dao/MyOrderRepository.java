package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.MyOrder;

import java.util.Optional;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {
        public Optional<MyOrder> findByOrderId(String orderId);
}
