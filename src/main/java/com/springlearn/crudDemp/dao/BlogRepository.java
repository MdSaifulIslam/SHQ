package com.springlearn.crudDemp.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.User;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
	
	List<Blog> findByUser(User user);
	
	Optional<Blog> findByUserAndId(User user, int post_id);
	
	List<Blog> findByIsApproved(boolean isApproved);
}
