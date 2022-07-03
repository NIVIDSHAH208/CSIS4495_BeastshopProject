package com.beastshop.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
	
	
	//Spring data jpa method - no need to write custom query
	public List<Currency> findAllByOrderByNameAsc();

}
