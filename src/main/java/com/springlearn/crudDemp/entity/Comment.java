package com.springlearn.crudDemp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "comment_tbl")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "comment", nullable = false)
	private String comment;

	@Column(name = "userid")
	private int userid;
	
	@Column(name = "username")
	private String username;
	
	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "post_id")
	private Blog blog;

	public Comment() {
	}

	public Comment(String comment) {
		this.comment = comment;
	}


	public Comment(String comment, int user, Blog blog) {
		this.comment = comment;
		this.userid = user;
		this.blog = blog;
	}

	public Comment(int id, String comment, int user, Blog blog) {
		this.id = id;
		this.comment = comment;
		this.userid = user;
		this.blog = blog;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", comment=" + comment + ", userid=" + userid + ", blog=" + blog + "]";
	}
	
}
