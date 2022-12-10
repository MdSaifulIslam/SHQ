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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springlearn.crudDemp.dao.UserRepository;
import com.springlearn.crudDemp.dto.AuthRequest;
import com.springlearn.crudDemp.dto.AuthResponse;
import com.springlearn.crudDemp.entity.Blog;
import com.springlearn.crudDemp.entity.Comment;
import com.springlearn.crudDemp.entity.User;
import com.springlearn.crudDemp.jwt.JwtTokenUtility;
import com.springlearn.crudDemp.service.BlogService;
import com.springlearn.crudDemp.service.CommentService;
import com.springlearn.crudDemp.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlogService blogService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private AuthenticationManager authenticationManager;
	private Authentication authentication;
	private String accessToken;

	@Autowired
	private JwtTokenUtility jwtTokenUtility;

	@Value("${app.social.scerete}")
	private String socialSecreteKey;

	@PostMapping("/auth")
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

	@GetMapping("/users")
	public List<User> getUser() {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/* User userData = */userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
		System.out.println(userService.findAll());

		return userService.findAll();
	}

	@PostMapping("/users")
	public User createNewUser(@RequestBody User userData) {

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

		return userService.saveUser(userData);
	}

	@DeleteMapping("/users")
	public String deleteUsers() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		userRepository.delete(userData);
		return "Deleted";
	}

	@PostMapping("/posts")
	public String savePost(@RequestBody Blog blog) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		blog.setApproevd(false);

		userData.addBlog(blog);
		userService.saveUser(userData);

		return "saved";
	}

	@GetMapping("/posts")
	public List<Blog> getPost() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		System.out.println(blogService.findByUser(userData));
		return blogService.findByUser(userData);
	}

	@PutMapping("/posts/approve/{post_id}")
	public String approvePost(@PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findByUserAndId(userData, post_id)
				.orElseThrow(() -> new RuntimeException("Post not found"));
		blog.setApproevd(true);
		blogService.saveBlog(blog);
		return "Blog Approved";
	}

	@DeleteMapping("/posts/{post_id}")
	public String deletePost(@PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userData = userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findByUserAndId(userData, post_id)
				.orElseThrow(() -> new RuntimeException("Post not found"));
		blogService.deletePost(blog);
		return "Blog deleted";
	}

	@PostMapping("/comments/{post_id}")
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

	@GetMapping("/comments/{post_id}")
	public Blog getComments(@PathVariable int post_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		Blog blog = blogService.findBbyId(post_id).orElseThrow(() -> new UsernameNotFoundException("Post Not Found"));

		return blog;
	}

	@GetMapping("/users/deactivate")
	public List<User> getDeactivatedUsers() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		return userService.findByUserRoleIdAndIsActive(1, false);
	}

	@GetMapping("/users/deactivate/post")
	public List<Blog> getDeactivatedPost() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.findByUsername(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		return blogService.findByIsApproved(true);
	}
}