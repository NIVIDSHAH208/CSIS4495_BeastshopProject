package com.beastshop.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beastshop.admin.FileUploadUtil;
import com.beastshop.admin.security.BeastshopUserDetails;
import com.beastshop.admin.user.UserService;
import com.beastshop.common.entity.User;

@Controller
public class AccountController {
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal BeastshopUserDetails loggedUserDetails, Model model) {
		String email=loggedUserDetails.getUsername();
		User user=service.getByEmail(email);
		model.addAttribute("user",user);
		return "users/account_form";
	}
	
	//Method to save the user to the database
		@PostMapping("/account/update")
		public String saveUser(User user, RedirectAttributes redirectAttributes,
				@AuthenticationPrincipal BeastshopUserDetails loggedUser,
				@RequestParam("image") MultipartFile multipartFile) throws IOException {
//			System.out.println(user);
//			System.out.println(multipartFile.getOriginalFilename());
			if (!multipartFile.isEmpty()) {

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				user.setPhotos(fileName);
				User savedUser = service.updateAccount(user);
				String uploadDir = "user-photos/" + savedUser.getId();
				FileUploadUtil.cleanDirectory(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} else {
				if (user.getPhotos().isEmpty()) {
					user.setPhotos(null);
				}
				service.updateAccount(user);
			}
			
			loggedUser.setFirstName(user.getFirstname());
			loggedUser.setLastName(user.getLastname());

			redirectAttributes.addFlashAttribute("message", "Your account details have been Updated successfully.");
			return "redirect:/account";
		}
}
