package com.springlearn.crudDemp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springlearn.crudDemp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	Optional<User> findById(int id);
	
	List<User> findByUserRoleIdAndIsActive(int userRole, boolean isActive);

	
	
}
