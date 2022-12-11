package com.springlearn.crudDemp.service;

import java.util.List;
import java.util.Optional;

import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.User;

public interface BlogService {

	public Blog saveBlog(Blog blog);

	public List<Blog> findAll();

	public List<Blog> findByUser(User userData);
	
	public Optional<Blog> findBbyId(int id);
	
	public Optional<Blog> findByUserAndId(User user, int post_id);
	
	public List<Blog> findByIsApproved(boolean isApproved);

	public void deletePost(Blog blog);

	public List<Blog> findByIsApprovedAndUser(boolean approve, User user);

}
