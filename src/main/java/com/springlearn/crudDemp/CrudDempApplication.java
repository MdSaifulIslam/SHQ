package com.springlearn.crudDemp;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springlearn.crudDemp.dao.UserRepository;
import com.springlearn.crudDemp.entity.User;

@SpringBootApplication
public class CrudDempApplication {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${app.user.admin}")
	private int adminRole;

	@PostConstruct
	public void initUsers() {

		List<User> users = Stream.of(new User("Admin user 01", "email1", adminRole, true, "user1", encoder.encode("pass")),
				new User("Admin user 02", "email2", adminRole, true, "user2", encoder.encode("pass"))).collect(Collectors.toList());
		repository.saveAll(users);
	}

	public static void main(String[] args) {
		SpringApplication.run(CrudDempApplication.class, args);
	}

}
