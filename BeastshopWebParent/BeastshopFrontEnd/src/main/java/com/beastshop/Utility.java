package com.beastshop;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.beastshop.security.oauth.CustomerOAuth2User;
import com.beastshop.setting.EmailSettingBag;

public class Utility {
	
	//Only return site url, and remove the other context path or extra url
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(),"");
	}
	
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag setting) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(setting.getHost());
		mailSender.setPort(setting.getPort());
		mailSender.setUsername(setting.getUsername());
		mailSender.setPassword(setting.getPassword());
		
		Properties mailProperties =new Properties();
		
		mailProperties.setProperty("mail.smtp.auth", setting.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", setting.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
	
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		if(principal==null) {
			return null;
		}
		
		String customerEmail = null;
		if(principal instanceof UsernamePasswordAuthenticationToken ||principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User)oAuth2Token.getPrincipal();
			customerEmail = oAuth2User.getEmail();
		}
		
		return customerEmail;
	}
	
}
