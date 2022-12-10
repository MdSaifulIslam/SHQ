package com.springlearn.crudDemp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "blog_tbl")
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title", unique = true, nullable = false)
	private String title;

	@Column(name = "blog_data", nullable = false)
	private String blogData;
	
	@Column(name = "is_approved")
	private boolean isApproved;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "blog_auth_id")
	private User user;

	@JsonManagedReference
	@JsonIgnore
	@OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private List<Comment> comments = new ArrayList<>();

	public Blog() {
	}

	public Blog(String title, String blogData) {
		this.title = title;
		this.blogData = blogData;
	}

	public Blog(String title, String blogData, boolean isApproevd) {
		this.title = title;
		this.blogData = blogData;
		this.isApproved = isApproevd;
	}

	public Blog(String title, String blogData, boolean isApproved, User user, List<Comment> comments) {
		this.title = title;
		this.blogData = blogData;
		this.isApproved = isApproved;
		this.user = user;
		this.comments = comments;
	}

	public Blog(int id, String title, String blogData, boolean isApproved, User user, List<Comment> comments) {
		this.id = id;
		this.title = title;
		this.blogData = blogData;
		this.isApproved = isApproved;
		this.user = user;
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBlogData() {
		return blogData;
	}

	public void setBlogData(String blogData) {
		this.blogData = blogData;
	}

	public boolean isApproevd() {
		return isApproved;
	}

	public void setApproevd(boolean isApproevd) {
		this.isApproved = isApproevd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void addComment(Comment comment) {

		if (comments == null) {
			comments = new ArrayList<>();
		}

		comments.add(comment);

		comment.setBlog(this);
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", blogData=" + blogData + ", user=" + user + "]";
	}

}