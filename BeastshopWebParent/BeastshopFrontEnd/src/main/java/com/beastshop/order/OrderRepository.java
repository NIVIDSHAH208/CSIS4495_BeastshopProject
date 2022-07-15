package com.beastshop.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beastshop.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
