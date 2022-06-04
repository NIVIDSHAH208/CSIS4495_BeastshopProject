package com.beastshop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Role;
import com.beastshop.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//Method to return the list of users
	public List<User> listAll(){
		return (List<User>)userRepo.findAll();
	}
	
	//Method to return list of roles object from the database
	public List<Role> listAllRoles(){
		return (List<Role>)roleRepo.findAll();
	}

	public void save(User user) {
		encodePassword(user);
		userRepo.save(user);
		
	}
	
	//method to encode the user password
	private void encodePassword(User user) {
		String encodedPasswordString = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPasswordString);
	}
	
}
