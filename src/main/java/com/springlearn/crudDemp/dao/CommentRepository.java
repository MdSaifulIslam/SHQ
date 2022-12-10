package com.springlearn.crudDemp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findByBlog(Blog blog);
}
