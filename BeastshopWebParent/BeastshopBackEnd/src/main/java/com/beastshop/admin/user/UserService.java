package com.beastshop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.admin.paging.PagingAndSortingHelper;
import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE=5;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//method to return user object by email
	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}

	// Method to return the list of users
	public List<User> listAll() {
		//We can also change the field and sorting type based on our requirements
		return (List<User>) userRepo.findAll(Sort.by("firstname").ascending());
	}
	
	//method returns page of user objects
	public void listByPage(int pageNumber, PagingAndSortingHelper helper){
		helper.listEntities(pageNumber, USERS_PER_PAGE, userRepo);
	}
	
	//Method to retreive details of currently logged in user
	public User updateAccount(User userInForm) {
		User userInDatabase = userRepo.findById(userInForm.getId()).get();
		if(!userInDatabase.getPassword().isEmpty()) {
			userInDatabase.setPassword(userInForm.getPassword());
			encodePassword(userInDatabase);
		}
		if(userInForm.getPhotos()!=null) {
			userInDatabase.setPhotos(userInForm.getPhotos());
		}
		userInDatabase.setFirstname(userInForm.getFirstname());
		userInDatabase.setLastname(userInForm.getLastname());
		return userRepo.save(userInDatabase);
	}
	

	// Method to return list of roles object from the database
	public List<Role> listAllRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {

		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepo.save(user);
		
	}

	// method to encode the user password
	private void encodePassword(User user) {
		String encodedPasswordString = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPasswordString);
	}

	// method to check the uniqueness of the user
	public boolean isEmailUnique(Integer id, String email) {
		// if user exists then it will return false, otherwise it will return true
		User userByEmail = userRepo.getUserByEmail(email);
		if (userByEmail == null) {
			return true;
		}
		boolean isCreatingNew = (id == null);
		// if we are creating new user, if email is used then --> false
		if (isCreatingNew) {
			if (userByEmail != null) {
				return false;
			}
		}
		// for the edited user, if id is not the same that means it is not the same user
		else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}

		return true;
	}
	
	//Method to delete the user
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if(countById==null||countById==0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}else {
			userRepo.deleteById(id);
		}
	}
	
	//Update User enable status
	public void updateUserEnableStatus(Integer id, boolean enable) {
		userRepo.updateEnabledStatus(id, enable);
	}

	
	// Method to get the user by id in order to update
	public User getById(Integer id) throws UserNotFoundException {

		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

	}
	
	
	
}
