package com.springlearn.crudDemp.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user_tbl")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "role_id")
	private int userRoleId;

	@Column(name = "is_actice")
	private boolean isActive;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password")
	private String password;

	@JsonManagedReference
	@JsonIgnore
	@OneToMany(targetEntity = Blog.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "blog_auth_id", referencedColumnName = "id")
	private List<Blog> blogs = new ArrayList<>();

	public User() {
	}

	public User(String name, String email, int userRoleId, boolean isActive, String username, String password) {
		this.name = name;
		this.email = email;
		this.userRoleId = userRoleId;
		this.isActive = isActive;
		this.username = username;
		this.password = password;
	}

	public User(String name, String email, int userRoleId, boolean isActive, String username, String password,
			List<Blog> blogs) {
		this.name = name;
		this.email = email;
		this.userRoleId = userRoleId;
		this.isActive = isActive;
		this.username = username;
		this.password = password;
		this.blogs = blogs;
	}

	public User(int id, String name, String email, int userRoleId, boolean isActive, String username, String password,
			List<Blog> blogs) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.userRoleId = userRoleId;
		this.isActive = isActive;
		this.username = username;
		this.password = password;
		this.blogs = blogs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", userRoleId=" + userRoleId + ", username="
				+ username + ", password=" + password + "]";
	}

	public void addBlog(Blog blog) {

		if (blogs == null) {
			blogs = new ArrayList<>();
		}

		blogs.add(blog);

		blog.setUser(this);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
