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
		.antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin","Salesperson")
			.antMatchers("/users/**","/customers/**","/settings/**", "/countries/**","/states/**").hasAuthority("Admin")
			.antMatchers("/categories/**","/brands/**","/article/**","/menus/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/products/edit/**","/products/save","/products/check_unique")
					.hasAnyAuthority("Admin","Editor","Salesperson")
			.antMatchers("/products", "/products/","/products/detail/**","/products/page/**")
					.hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
			
			.antMatchers("/products/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/orders", "/orders/","/orders/detail/**","/orders/page/**").hasAnyAuthority("Admin","Salesperson","Shipper")
			.antMatchers("/customers/**","/shipping/**","/orders/**","/report/**","/get_shipping_cost").hasAnyAuthority("Admin","Salesperson")
			.antMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
				.anyRequest()
				.authenticated()
					.and()
					.formLogin()
					.loginPage("/login")
					.usernameParameter("email")
					.permitAll()
					.and().logout().permitAll().and().rememberMe().key("AbcdEfghIjkLmnOp-1234567890")
					.tokenValiditySeconds(7*24*60*60);
		http.headers().frameOptions().sameOrigin();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

}
