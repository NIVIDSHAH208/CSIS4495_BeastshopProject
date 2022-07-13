package com.beastshop.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Address;
import com.beastshop.common.entity.Customer;

@Service
public class AddressService {
	@Autowired private AddressRepository repo;
	
	
	//Method to find the authenticated customer
	public List<Address> listAddressBook(Customer customer){
		return repo.findByCustomer(customer);
	}
}
