package com.beastshop.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.beastshop.admin.setting.country.CountryRepository;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE=10;
	
	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	
	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc")?sort.ascending():sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, CUSTOMER_PER_PAGE, sort);
		
		if(keyword!=null) {
			return customerRepo.findAll(keyword, pageable);
		}
		return customerRepo.findAll(pageable);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException{
		try {
			return customerRepo.findById(id).get();
		}catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customer with id: "+id);
		}
	}
	
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);
		
		if(existCustomer!=null&&existCustomer.getId()!=id) {
			return false;
		}
		return true;
	}
	
	public void save(Customer customerInForm) {
		if(!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		}else {
			Customer customerInDb = customerRepo.findById(customerInForm.getId()).get();
			customerInForm.setPassword(customerInDb.getPassword());
		}
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException{
		Long count = customerRepo.countById(id);
		
		if(count==null||count==0) {
			throw new CustomerNotFoundException("Could not find any customer with id: "+id);
		}
		customerRepo.deleteById(id);
	}
	
	
	
}
