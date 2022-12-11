package com.springlearn.crudDemp.service;

import java.util.List;
import java.util.Optional;

import com.springlearn.crudDemp.entity.User;

public interface UserService {

	public User saveUser(User user);

	public List<User> findAll();

	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmail(String eamil);
	
	public List<User> findByUserRoleIdAndIsActive(int userRole, boolean isActive);
	
	Optional<User> findById(int id);
	
	List<User> findByUserRoleId(int userRole);
}
