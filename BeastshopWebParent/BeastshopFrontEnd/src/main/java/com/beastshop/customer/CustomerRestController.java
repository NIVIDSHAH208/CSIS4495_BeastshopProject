package com.beastshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customer/check_unique_email")
	public String checkDuplicateEmail( String email) {
		return customerService.isEmailUnique(email)?"OK":"Duplicated";
	}
}
