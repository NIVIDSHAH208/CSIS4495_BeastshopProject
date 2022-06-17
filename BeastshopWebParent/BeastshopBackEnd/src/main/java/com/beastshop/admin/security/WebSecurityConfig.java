package com.beastshop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new BeastshopUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//We are telling that authentication will be based on the database
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/users/**").hasAuthority("Admin")
			.antMatchers("/categories/**").hasAuthority("Admin")
			.antMatchers("/categories/**").hasAuthority("Editor")
			.antMatchers("/brands/**").hasAuthority("Admin")
			.antMatchers("/brands/**").hasAuthority("Editor")
			.antMatchers("/products/**").hasAuthority("Admin")
			.antMatchers("/products/**").hasAuthority("Editor")
			.antMatchers("/products/**").hasAuthority("Salesperson")
			.antMatchers("/products/**").hasAuthority("Shipper")
			.antMatchers("/customers/**").hasAuthority("Admin")
			.antMatchers("/orders/**").hasAuthority("Admin")
			.antMatchers("/orders/**").hasAuthority("Salesperson")
			.antMatchers("/orders/**").hasAuthority("Shipper")
			.antMatchers("/shipping/**").hasAuthority("Admin")
			.antMatchers("/shipping/**").hasAuthority("Salesperson")
			.antMatchers("/report/**").hasAuthority("Admin")
			.antMatchers("/report/**").hasAuthority("Salesperson")
			.antMatchers("/article/**").hasAuthority("Admin")
			.antMatchers("/article/**").hasAuthority("Editor")
			.antMatchers("/menus/**").hasAuthority("Admin")
			.antMatchers("/menus/**").hasAuthority("Editor")
			.antMatchers("/settings/**").hasAuthority("Admin")
				.anyRequest()
				.authenticated()
					.and()
					.formLogin()
					.loginPage("/login")
					.usernameParameter("email")
					.permitAll()
					.and().logout().permitAll().and().rememberMe().key("AbcdEfghIjkLmnOp-1234567890")
					.tokenValiditySeconds(7*24*60*60);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

}
