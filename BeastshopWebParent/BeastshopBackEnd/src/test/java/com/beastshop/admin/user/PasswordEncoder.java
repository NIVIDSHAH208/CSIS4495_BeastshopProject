package com.beastshop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
	
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
		String passwordString = "nivid2020";
		
		String encodedPasswordString = passwordEncoder.encode(passwordString);
		System.out.println(encodedPasswordString);
		
		boolean matches=passwordEncoder.matches(passwordString, encodedPasswordString);
		assertThat(matches).isTrue();
	}

}
