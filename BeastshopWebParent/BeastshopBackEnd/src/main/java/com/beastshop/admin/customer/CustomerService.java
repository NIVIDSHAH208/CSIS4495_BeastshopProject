package com.beastshop.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.admin.paging.PagingAndSortingHelper;
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
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
	}
	
	public List<Customer> listUsersForCsv() {
		return (List<Customer>)customerRepo.findAll(Sort.by("id").ascending());
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
		Customer customerInDb = customerRepo.findById(customerInForm.getId()).get();
		if(!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		}else {
			customerInForm.setPassword(customerInDb.getPassword());
		}
		
		customerInForm.setEnabled(customerInDb.isEnabled());
		customerInForm.setCreatedTime(customerInDb.getCreatedTime());
		customerInForm.setVerificationCode(customerInDb.getVerificationCode());
		
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
