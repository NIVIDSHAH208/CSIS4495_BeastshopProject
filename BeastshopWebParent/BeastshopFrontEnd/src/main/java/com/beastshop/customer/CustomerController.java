package com.beastshop.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beastshop.Utility;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.Customer;
import com.beastshop.security.CustomerUserDetails;
import com.beastshop.security.oauth.CustomerOAuth2User;
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
		
//		System.out.println("To address: "+toAddresss);
//		System.out.println("Verify URL: "+verifyURL);
	}
	
	
	@GetMapping("/verify")
	public String verifyAccount( String code, Model model) {
		boolean verified = customerService.verify(code);
		
		return "register/"+(verified?"verify_success":"verify_fail");
	
	}
	
	
	@GetMapping("/account_details")
	public String viewAccountDetail(Model model, HttpServletRequest request) {
		
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("listCountries",listCountries);
		
		model.addAttribute("customer",customer);
		
		return "customer/account_form";
	}
	
	
	
	
	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra, HttpServletRequest request) {
		ra.addFlashAttribute("message","Your account details have been updated");
		customerService.update(customer);
		updateNameForAuthenticatedCustomer(customer, request);
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/account_details";
		if("address_book".equals(redirectOption)) {
			redirectURL="redirect:/address_book";
		} else if("cart".equals(redirectOption)) {
			redirectURL="redirect:/cart";
		}
		
		return redirectURL;
	}
	
	private void updateNameForAuthenticatedCustomer(Customer customer,HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		
		if(principal instanceof UsernamePasswordAuthenticationToken ||principal instanceof RememberMeAuthenticationToken) {
		
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
			
			
		}else if(principal instanceof OAuth2AuthenticationToken) {
			String fullName = customer.getFirstName()+" "+customer.getLastName();
			OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User)oAuth2Token.getPrincipal();
			oAuth2User.setFullName(fullName);
		}

	}
	
	
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails =(CustomerUserDetails) token.getPrincipal();
		}else if(principal instanceof RememberMeAuthenticationToken){
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails =(CustomerUserDetails) token.getPrincipal();
		}
		
		return userDetails;
	}
}
