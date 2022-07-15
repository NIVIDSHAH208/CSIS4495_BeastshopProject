package com.beastshop.shipping;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	
	//Data jpa method
	public ShippingRate findByCountryAndState(Country country, String state);
}
