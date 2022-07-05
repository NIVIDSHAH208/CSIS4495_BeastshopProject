package com.beastshop.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
}
