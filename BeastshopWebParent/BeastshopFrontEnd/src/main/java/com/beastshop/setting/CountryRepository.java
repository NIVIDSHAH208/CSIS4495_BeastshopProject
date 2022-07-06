package com.beastshop.setting;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Country;


public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
}
