package com.beastshop.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.beastshop.Utility;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.exception.CustomerNotFoundException;
import com.beastshop.setting.EmailSettingBag;
import com.beastshop.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request)+"/reset_password?token="+token;
			sendEmail(link, email);
			model.addAttribute("message", "Please check your email to reset your password");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error","Could not send email");
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddresss = email;
		String subject = "Link to reset your password | Beastshop";
		
		String content = "<p>Hello,</p>"+"<p>You have requested to reset your password.</p>"
		+"<p>click the link below to change your password</p>"+"<p><a href=\""+link+"\">Change password</a></p>"+
				"<p>Ignore if irrelevant</p>";
		
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
		helper.setTo(toAddresss);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		
		mailSender.send(message);
	}


	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if(customer!=null) {
			model.addAttribute("customer", customer);
			model.addAttribute("token",token);
		}else {
			model.addAttribute("pageTitle","Invalid token");
			model.addAttribute("message", "Invalid link or Invalid token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle","Success - Password reset");
			model.addAttribute("title","Reset your password");
			model.addAttribute("message","You have successfully changed your password");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","Invalid token");
			model.addAttribute("message", e.getMessage());
			return "message";
		}
	}
	
}
