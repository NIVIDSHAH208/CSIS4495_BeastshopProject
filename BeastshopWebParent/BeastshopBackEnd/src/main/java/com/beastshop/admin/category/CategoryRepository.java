package com.beastshop.admin.category;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.beastshop.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

}
