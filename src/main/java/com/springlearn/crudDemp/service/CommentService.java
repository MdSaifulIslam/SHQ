package com.springlearn.crudDemp.service;

import java.util.List;

import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.Comment;

public interface CommentService {

	public Comment saveComment(Comment comment);

	public List<Comment> findAll();

	public List<Comment> findByBlog(Blog blog);

}
