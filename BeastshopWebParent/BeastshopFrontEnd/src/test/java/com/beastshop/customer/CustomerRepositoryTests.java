package com.beastshop.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.AuthenticationType;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	@Autowired
	private CustomerRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234;
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Darshil");
		customer.setLastName("Jogani");
		customer.setPassword("darshil2020");
		customer.setEmail("darshil@jogani.com");
		customer.setPhoneNumber("604-778-1234");
		customer.setAddressLine1("West Edmonton Mall");
		customer.setCity("Edmonton");
		customer.setState("California");
		customer.setPostalCode("95678");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateCustomer2() {
		Integer countryId = 106;
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Yash");
		customer.setLastName("Shah");
		customer.setPassword("yash2020");
		customer.setEmail("yash@shah.com");
		customer.setPhoneNumber("987-654-1234");
		customer.setAddressLine1("101, Sun real homes-2");
		customer.setCity("Ahmedabad");
		customer.setVerificationCode("code_123");
		customer.setState("Gujarat");
		customer.setPostalCode("95678");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCustomer() {
		Integer cusId = 2;
		Optional<Customer> findById = repo.findById(cusId);
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer cusId = 2;
		repo.deleteById(cusId);
		
		Optional<Customer> findById = repo.findById(cusId);
		assertThat(findById).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email = "kushal@shah.com";
		Customer customer = repo.findByEmail(email);
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "code_123";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId=2;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
	
	@Test
	public void testUpdateAuthenticationType() {
		Integer id=1;
		repo.updateAuthenticationType(id, AuthenticationType.DATABASE);
		 Customer customer = repo.findById(id).get();
		
		assertThat(customer.getAuthenticationType().equals(AuthenticationType.DATABASE));
	}

}
