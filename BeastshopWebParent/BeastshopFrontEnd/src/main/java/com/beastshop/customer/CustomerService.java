package com.beastshop.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.AuthenticationType;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.exception.CustomerNotFoundException;
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
	private PasswordEncoder passwordEncoder;

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
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		// Randomstring comes in spring by default
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
//		System.out.println("Verification code is: "+randomCode);
		customerRepo.save(customer);
	}

	// Get customer by email
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	private void encodePassword(Customer customer) {
		// TODO Auto-generated method stub
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	// method to verify customer account and enable the customer
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}

	// Method to update the authentication type of the customer
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType) {

		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);

		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));

		customerRepo.save(customer);
	}

	// Set first and last name based on the name returned from the google login
	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if (nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}

	// method to update the customer
	public void update(Customer customerInForm) {
		Customer customerInDb = customerRepo.findById(customerInForm.getId()).get();

		if (customerInDb.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
				customerInForm.setPassword(encodedPassword);
			} else {
				customerInForm.setPassword(customerInDb.getPassword());
			}
		}else {
			customerInForm.setPassword(customerInDb.getPassword());
		}

		customerInForm.setEnabled(customerInDb.isEnabled());
		customerInForm.setCreatedTime(customerInDb.getCreatedTime());
		customerInForm.setVerificationCode(customerInDb.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDb.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDb.getResetPasswordToken());
		
		
		customerRepo.save(customerInForm);
	}

	//Method to append reset password token
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByEmail(email);
		if(customer!=null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);
			return token;
		}else {
			throw new CustomerNotFoundException("Cound not find any customer with email: "+email);
		}
	}
	
	//method to get customer by the token
	public Customer getByResetPasswordToken(String token) {
		return customerRepo.findByResetPasswordToken(token);
	}
	
	//method to update the password
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if(customer==null) {
			throw new CustomerNotFoundException("No customer found :: Invalid Token");
		}
		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		encodePassword(customer);
		customerRepo.save(customer);
	}

}
