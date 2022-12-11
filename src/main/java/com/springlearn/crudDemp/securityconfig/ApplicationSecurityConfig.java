package com.springlearn.crudDemp.securityconfig;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springlearn.crudDemp.dao.UserRepository;
import com.springlearn.crudDemp.jwt.JwtFilter;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("not found =====>>>>")));
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
	    http.headers().frameOptions().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.exceptionHandling().authenticationEntryPoint((req, res, ex) -> {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		});

		http.authorizeRequests().antMatchers(/*HttpMethod.POST,*/ "/api/auth", "/api/users/users" , "/api/all/posts", "/h2-console/**", "/api/all/posts").permitAll().anyRequest().authenticated();
	
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
