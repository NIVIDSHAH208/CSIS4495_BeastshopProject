package com.beastshop.admin.order;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.beastshop.common.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
	
}
