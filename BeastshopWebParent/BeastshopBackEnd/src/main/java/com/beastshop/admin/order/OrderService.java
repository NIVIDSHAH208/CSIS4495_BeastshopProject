package com.beastshop.admin.order;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.beastshop.admin.paging.PagingAndSortingHelper;
import com.beastshop.common.entity.Order;

@Service
public class OrderService {
	private static final int ORDERS_PER_PAGE=10;
	
	@Autowired private OrderRepository repo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		
		if("destination".equals(sortField)) {
			sort= Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		}else {
			sort = Sort.by(sortField);
		}
		
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page=null;
		
		if(keyword!=null) {
			page=repo.findAll(keyword, pageable);
		}else {
			page=repo.findAll(pageable);
		}
		helper.updateModelAttributes(pageNum, page);
	}
	
	public Order get(Integer id) throws OrderNotFoundException{
		try {
			return repo.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new OrderNotFoundException("Could not find any product with ID: "+id);
		}
	}
}
