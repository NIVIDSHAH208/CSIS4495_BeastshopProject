package com.beastshop.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

public class BeastshopUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user; 
	
	
	
	public BeastshopUserDetails(User user) {
		this.user = user;
	}

	//This method below gets the assigned roles to the below user
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for(Role role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}
	
	
	public String getFullName() {
		return this.user.getFirstname()+" "+this.user.getLastname();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstname(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setLastname(lastName);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}

}
