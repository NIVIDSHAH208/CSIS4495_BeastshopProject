package com.beastshop;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

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
	
}
