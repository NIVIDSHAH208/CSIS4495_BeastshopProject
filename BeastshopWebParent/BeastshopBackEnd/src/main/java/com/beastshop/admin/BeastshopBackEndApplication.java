package com.beastshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.beastshop.common.entity","com.beastshop.admin.user"})
public class BeastshopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeastshopBackEndApplication.class, args);
	}

}
