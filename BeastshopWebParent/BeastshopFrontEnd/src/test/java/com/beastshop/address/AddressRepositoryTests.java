package com.beastshop.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.Address;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	@Autowired
	private AddressRepository addressRepo;

	@Test
	public void testAddNew() {
		Integer customerId = 39;
		Integer countryId = 106;

		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Nivid N.");
		newAddress.setLastName("Shah");
		newAddress.setPhoneNumber("7043200825");
		newAddress.setAddressLine1("B-101, Sun real homes-2");
		newAddress.setCity("Ahmedabad");
		newAddress.setState("Gujarat");
		newAddress.setPostalCode("382470");
		Address savedAddress = addressRepo.save(newAddress);

		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);

	}

	@Test
	public void testFindByCustomer() {
		Integer customerId = 2;
		List<Address> listAddresses = addressRepo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);

		listAddresses.forEach(System.out::println);

	}

	@Test
	public void testFindByIdAndCustomer() {
		Integer customerId = 2;
		Integer addressId = 1;

		Address address = addressRepo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNotNull();
		System.out.println(address);

	}

	@Test
	public void testUpdate() {
		Integer addressId = 4;
		Address address = addressRepo.findById(addressId).get();
//		String phoneNumber = "123456789";
		address.setDefaultForShipping(true);

		Address savedAddress = addressRepo.save(address);
		assertThat(savedAddress.isDefaultForShipping()).isEqualTo(true);
	}

	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 1;
		Integer customerId = 2;
		addressRepo.deleteByIdAndCustomer(addressId, customerId);
		Address findByIdAndCustomer = addressRepo.findByIdAndCustomer(addressId, customerId);
		assertThat(findByIdAndCustomer).isNull();
	}
	
	@Test
	public void testSetDefault() {
		Integer addressId = 7;
		addressRepo.setDefaultAddress(addressId);
		
		
		Address address = addressRepo.findById(addressId).get();
		assertThat(address.isDefaultForShipping());
	}
	
	@Test
	public void testSetNoneDefault() {
		Integer addressId = 7;
		Integer customerId = 39;
		addressRepo.setNonDefaultForOthers(addressId, customerId);
		
	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 39;
		Address address = addressRepo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
	}

}
