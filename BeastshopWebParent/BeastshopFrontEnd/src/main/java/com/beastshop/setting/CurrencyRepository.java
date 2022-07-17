package com.beastshop.setting;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
