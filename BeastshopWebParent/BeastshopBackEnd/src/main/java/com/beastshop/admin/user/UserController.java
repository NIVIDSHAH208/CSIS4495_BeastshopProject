package com.beastshop.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beastshop.admin.FileUploadUtil;
import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "id", "asc",null);
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listAllRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create new User");
		return "user_form";
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		System.out.println("Sort field: "+sortField+" Sort Dir: "+sortDir);
		Page<User> pageUser = service.listByPage(pageNum, sortField, sortDir,keyword);
		List<User> listUsers = pageUser.getContent();
		// Printing to check the page and user sizes
//		System.out.println("Page number is: "+pageNum);
//		System.out.println("Total element are: "+pageUser.getTotalElements());
//		System.out.println("Total Pages are: "+pageUser.getTotalPages());
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;

		if (endCount > pageUser.getTotalElements()) {
			endCount = pageUser.getTotalElements();
		}
		String reverseSortDir= sortDir.equals("asc")?"desc":"asc";
		model.addAttribute("totalPages", pageUser.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageUser.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);


		return "users";

	}

	//Method to save the user to the database
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
//		System.out.println(user);
//		System.out.println(multipartFile.getOriginalFilename());
		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		return getRedirectedURLtoAffectedUser(user);
	}

	private String getRedirectedURLtoAffectedUser(User user) {
		String firstPartofEmail=user.getEmail().split("@")[0];
				
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartofEmail;
	}

	
	//Methods to delete and edit the users
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "User ID: " + id + " has been deleted successfully!");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			List<Role> listRoles = service.listAllRoles();
			User user = service.getById(id);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}

	}

	//Method to enable or disable the user on click
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		service.updateUserEnableStatus(id, enabled);
		String status = enabled ? "Enabled" : "Disabled";
		String userMessage = "The user Id: " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", userMessage);
		return "redirect:/users";
	}
	
	
	//New method to handle the export to CSV
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listAllUsers=service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listAllUsers, response);
	}
	
}
