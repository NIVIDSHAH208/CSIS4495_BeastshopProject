package com.beastshop.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {
	
	private OAuth2User oAuth2User;
	
	

	public CustomerOAuth2User(OAuth2User oAuth2User) {
		this.oAuth2User = oAuth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttribute("name");
	}

	
	public String getEmail() {
		return oAuth2User.getAttribute("email");
	}
	
	public String getFullName() {
		return oAuth2User.getAttribute("name");
	}
	
}
