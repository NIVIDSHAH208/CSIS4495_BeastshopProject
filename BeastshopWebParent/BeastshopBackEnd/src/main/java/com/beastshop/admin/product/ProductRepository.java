package com.beastshop.admin.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.beastshop.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
