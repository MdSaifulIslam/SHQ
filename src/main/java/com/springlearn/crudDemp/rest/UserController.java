package com.springlearn.crudDemp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springlearn.crudDemp.dto.AuthRequest;
import com.springlearn.crudDemp.dto.AuthResponse;
import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.Comment;
import com.springlearn.crudDemp.entity.User;
import com.springlearn.crudDemp.jwt.JwtTokenUtility;
import com.springlearn.crudDemp.service.BlogService;
import com.springlearn.crudDemp.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private BlogService blogService;

	@Value("${app.user.admin}")
	private int adminRole;

	@Autowired
	private AuthenticationManager authenticationManager;
	private Authentication authentication;
	private String accessToken;

	@Autowired
	private JwtTokenUtility jwtTokenUtility;

	@Value("${app.social.scerete}")
	private String socialSecreteKey;

	@PostMapping("/all/auth")
	public AuthResponse login(@RequestBody AuthRequest authRequest) {

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			User user = (User) authentication.getPrincipal();
			System.out.println(user);

			accessToken = jwtTokenUtility.generateAccessToken(user);

			return new AuthResponse(user.getEmail(), accessToken);

		} catch (BadCredentialsException e) {
			throw new RuntimeException("Username/Password not found");
		}
	}

	// Get all user list
	@GetMapping("/admin/users")
	public List<User> getUser() {

		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		return userService.findByUserRoleId(0);
	}

	// Get active or de-active users by simply passing true/ false
	@GetMapping("/admin/users/{approve}")
	public List<User> getUser(@PathVariable boolean approve) {

		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		return userService.findByUserRoleIdAndIsActive(0, approve); // 0 for user, 1 for admin
	}

	// get the all approved or disapproved posts
	@GetMapping("/admin/posts/{approve}")
	public List<Blog> getPost(@PathVariable boolean approve) {

		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		return blogService.findByIsApproved(approve);
	}

	// Approve or disapprove post by post id
	@PutMapping("/admin/posts/{post_id}")
	public String approvePost(@PathVariable int post_id) {

		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		Blog blog = blogService.findBbyId(post_id).orElseThrow(() -> new RuntimeException("Post not found"));

		blog.setApproevd(!blog.isApproevd()); // make active to deactivate and vise-versa
		blogService.saveBlog(blog);

		return "Blog Updated";
	}

	// active or deactive user by id
	@PutMapping("/admin/users/{user_id}")
	public String approveUser(@PathVariable int user_id) {

		System.out.println("found");
		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		User user = userService.findById(user_id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		user.setActive(!user.isActive());
		userService.saveUser(user);

		return "User Updated";
	}

	// add new admin
	@PostMapping("/admin/admin")
	public String addAdmin(@RequestBody User user) {

		if (!isAuthorized(adminRole)) {
			throw new RuntimeException("Not Authorised user");
		}

		if (userService.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username already taken, Please choose different username");
		}
		if (userService.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email already taken, Please choose different username");
		}
		if (user.getPassword() == "") {
			throw new RuntimeException("Password is null, please insert password");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setUserRoleId(1);

		user.setActive(true);
		userService.saveUser(user);

		return "Admin added";
	}

	// create new account of user
	@PostMapping("/users/users")
	public String createNewUser(@RequestBody User userData) {

		if (userService.findByUsername(userData.getUsername()).isPresent()) {
			throw new RuntimeException("Username already taken, Please choose different username");
		}
		if (userService.findByEmail(userData.getEmail()).isPresent()) {
			throw new RuntimeException("Email already taken, Please choose different username");
		}
		if (userData.getPassword() == "") {
			throw new RuntimeException("Password is null, please insert password");
		}

		userData.setPassword(passwordEncoder.encode(userData.getPassword()));
		userData.setUserRoleId(0);
		userData.setActive(false);

		userService.saveUser(userData);

		return "Your account is created, please wait for admin approval";
	}

	@PostMapping("/users/posts")
	public String savePost(@RequestBody Blog blog) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		if (userData.isActive() == false) {
			throw new RuntimeException("You are not active user, Please wait for admin approval");
		}

		blog.setApproevd(false);

		userData.addBlog(blog);
		userService.saveUser(userData);

		return "Post saved and wait for admin approval";
	}

	// get all users posts
	@GetMapping("/users/posts")
	public List<Blog> getPost() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		return blogService.findByUser(userData);
	}

	@DeleteMapping("/users/posts/{post_id}")
	public String deletePost(@PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findByUserAndId(userData, post_id)
				.orElseThrow(() -> new RuntimeException("Post not found"));

		blogService.deletePost(blog);

		return "Blog deleted";
	}

	// find users approved or disapproved posts
	@GetMapping("/users/posts/{approved}")
	public List<Blog> getPostList(@PathVariable boolean approve) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		return blogService.findByIsApprovedAndUser(approve, userData);
	}

	// add comment to a post
	@PostMapping("/users/comments/{post_id}")
	public String saveComments(@RequestBody Comment comment, @PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findBbyId(post_id).orElseThrow(() -> new UsernameNotFoundException("Post Not Found"));
		if (blog.isApproevd() == false) {
			throw new RuntimeException("Post is not Approved");
		}

		comment.setUserid(userData.getId());
		comment.setUsername(userData.getUsername());

		blog.addComment(comment);
		blogService.saveBlog(blog);

		return "comment saved";
	}

	// get all approved posts
	@GetMapping("/all/posts")
	public List<Blog> getAllApprovedPosts() {
		return blogService.findByIsApproved(true);
	}

	// get a post and it's comments
	@GetMapping("/all/posts/{post_id}")
	public Blog getComments(@PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findByUserAndId(userData, post_id)
				.orElseThrow(() -> new UsernameNotFoundException("Post Not Found"));

		return blog;
	}

	public boolean isAuthorized(int role) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
		if (userData.getUserRoleId() == role) {
			return true;
		}
		return false;
	}
}