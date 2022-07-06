package com.beastshop.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;
import com.beastshop.setting.CountryRepository;

@Service
public class CustomerService {
	@Autowired
	private CountryRepository countryRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	public List<Country> listAllCountries(){
		List<Country> findAllByOrderByNameAsc = countryRepo.findAllByOrderByNameAsc();
		return findAllByOrderByNameAsc;
	}
	
	//If customer exists, then  the object will not be null
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer==null;
	}
}
