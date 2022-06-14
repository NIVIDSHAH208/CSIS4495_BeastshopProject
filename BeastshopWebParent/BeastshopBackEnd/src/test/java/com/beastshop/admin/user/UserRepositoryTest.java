package com.beastshop.admin.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;
import static org.hamcrest.CoreMatchers.theInstance;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.swing.Spring;
import javax.swing.event.TreeWillExpandListener;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.RepositoryFragment.ImplementedRepositoryFragment;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

import net.bytebuddy.asm.Advice.This;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	public void testCreateUser() {
//		This method will be Implemented by Spring data jpa	
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userNividUser  = new User("nivid@nivid.com", "nivid2020", "Nivid", "Shah");
		userNividUser.addRole(roleAdmin);
		
		User savedUser = repo.save(userNividUser);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
//	test method to add multiple users
	@Test
	public void testCreateUserWithTwoRoles() {
		User userAkshay = new User("akshay@shakhia.com", "akshay2020", "Akshay", "Shakhia");
//		Assigning two roles to the user
		Role roleEditor = new Role(3); 
		Role roleAssistant = new Role(5); 
		userAkshay.addRole(roleEditor);
		userAkshay.addRole(roleAssistant);
		
		User savedUser = repo.save(userAkshay);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
//	test method to retrieve all users from the database
	@Test
	public void testListAllUsers() {
		
		 Iterable<User> listUsers =repo.findAll();
		 
		 listUsers.forEach(user->System.out.println(user));
		
	}
	
	
//	get user information based on userID
	@Test
	public void getUserById() {
		User retrievedUser = repo.findById(1).get();
		System.out.println(retrievedUser);
		assertThat(retrievedUser).isNot(null);
		
	}
	
//	Method to update the user information in the database
	@Test
	public void testUpdateUserDetails() {
		User retrievedUser = repo.findById(2).get();
		retrievedUser.setEnabled(true);
		retrievedUser.setEmail("akshay@great.com");
		repo.save(retrievedUser);
	}
	
	//test to update roles
	@Test
	public void testUpdateUserRoles() {
		User retrievedUser = repo.findById(2).get();
		Role roleEditoRole = new Role(3);
		Role roleSalesPersonRole = new Role(2);
		
		retrievedUser.getRoles().remove(roleEditoRole);
		retrievedUser.addRole(roleSalesPersonRole);
		
		repo.save(retrievedUser);
	}
	
	//test to delete the existing user
	@Test
	public void testDeleteUser() {
		Integer userIdtoDeleteInteger = 2;
		repo.deleteById(userIdtoDeleteInteger);
		
	}
	
	//Test method to check the uniqueness of email
	//find user having specific email
	@Test
	public void testGetUserByEmail() {
		String emailString = "anjali@anjali.com";
		User user= repo.getUserByEmail(emailString);
		assertThat(user).isNotNull();
	}
	
	//Test method to get the count by Id
	@Test
	public void testCountById() {
		Integer id=3;
		Long count = repo.countById(id);
		assertThat(count).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id=14;
		repo.updateEnabledStatus(id, false);
	}
	@Test
	public void testEnableUser() {
		Integer id=13;
		repo.updateEnabledStatus(id, true);
	}
	
	//We want to test the pagination
	@Test
	public void testListFirstPage() {
		int pageNum=0;
		int pageSize=4;
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<User> page=repo.findAll(pageable);
		//get the content of the page
		List<User> listUsers=page.getContent();
		listUsers.forEach(user->System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	
}
