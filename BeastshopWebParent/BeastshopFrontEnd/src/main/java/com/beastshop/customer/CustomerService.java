package com.beastshop.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;
import com.beastshop.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	public List<Country> listAllCountries() {
		List<Country> findAllByOrderByNameAsc = countryRepo.findAllByOrderByNameAsc();
		return findAllByOrderByNameAsc;
	}

	// If customer exists, then the object will not be null
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	// Register customer and encode the password
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());

		// Randomstring comes in spring by default
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
//		System.out.println("Verification code is: "+randomCode);
		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		// TODO Auto-generated method stub
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	// method to verify customer account and enable the customer
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
		if(customer==null||customer.isEnabled()) {
			return false;
		}else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
}
