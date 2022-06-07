package com.beastshop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Method to return the list of users
	public List<User> listAll() {
		return (List<User>) userRepo.findAll();
	}

	// Method to return list of roles object from the database
	public List<Role> listAllRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {

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
		userRepo.save(user);

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
