package com.beastshop.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.beastshop.Utility;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;
import com.beastshop.setting.EmailSettingBag;
import com.beastshop.setting.SettingService;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle","Customer registration");
		model.addAttribute("customer", new Customer());
		
		return "register/register_form";
	}
	
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		
		model.addAttribute("pageTitle", "Registration Succeeded");
		return "register/register_success";
	}


	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddresss = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
		helper.setTo(toAddresss);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullname());
		String verifyURL = Utility.getSiteURL(request)+"/verify?code="+customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		mailSender.send(message);
		
		System.out.println("To address: "+toAddresss);
		System.out.println("Verify URL: "+verifyURL);
		
	}
}
