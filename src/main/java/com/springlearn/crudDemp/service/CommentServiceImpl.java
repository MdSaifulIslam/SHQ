package com.springlearn.crudDemp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springlearn.crudDemp.dao.CommentRepository;
import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.Comment;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Override
	public List<Comment> findByBlog(Blog blog) {
		return commentRepository.findByBlog(blog);
	}
}
