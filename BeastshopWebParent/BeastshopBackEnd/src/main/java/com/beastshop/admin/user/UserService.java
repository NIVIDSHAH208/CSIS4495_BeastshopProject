package com.beastshop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	//Method to return the list of users
	public List<User> listAll(){
		return (List<User>)repo.findAll();
	}
}
