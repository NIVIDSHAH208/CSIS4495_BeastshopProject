package com.beastshop.setting;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Country;


public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
	
	//return country object based on country code
	@Query("SELECT c FROM Country c WHERE c.code =?1")
	public Country findByCode(String code);
}
