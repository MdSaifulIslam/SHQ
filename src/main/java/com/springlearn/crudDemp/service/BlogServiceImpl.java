package com.springlearn.crudDemp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springlearn.crudDemp.dao.BlogRepository;
import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.User;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Override
	public List<Blog> findAll() {
		return blogRepository.findAll();
	}


	@Override
	public Blog saveBlog(Blog blog) {
		return blogRepository.save(blog);
	}


	@Override
	public List<Blog> findByUser(User userData) {
		System.out.println(blogRepository.findByUser(userData));
		return blogRepository.findByUser(userData);
	}


	@Override
	public Optional<Blog> findBbyId(int id) {
		return blogRepository.findById(id);
	}


	@Override
	public Optional<Blog> findByUserAndId(User user, int post_id) {
		return blogRepository.findByUserAndId(user, post_id);
	}


	@Override
	public void deletePost(Blog blog) {
		blogRepository.delete(blog);
	}


	@Override
	public List<Blog> findByIsApproved(boolean isApproved) {
		return blogRepository.findByIsApproved(isApproved);
	}

}
