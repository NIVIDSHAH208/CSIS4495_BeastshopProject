package com.beastshop.admin.brand;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.beastshop.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	public Long countById(Integer id);
}
