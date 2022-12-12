package com.springlearn.crudDemp.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springlearn.crudDemp.entity.User;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtility jwtTokenUtility;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//		LocalDateTime now = LocalDateTime.now();
//		StringBuilder sb = new StringBuilder();
//
//	    sb.append("Request Method = [" + request.getMethod() + "], ");
//	    sb.append("Request URL Path = [" + request.getRequestURL() + "], \n");
//
//	    String headers =
//	        Collections.list(request.getHeaderNames()).stream()
//	            .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)) )
//	            .collect(Collectors.joining(", "));
//
//	    if (headers.isEmpty()) {
//	        sb.append("Request headers: NONE,");
//	    } else {
//	        sb.append("Request headers: ["+headers+"],\n");
//	    }
//
//	    String parameters =
//	        Collections.list(request.getParameterNames()).stream()
//	            .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
//	            .collect(Collectors.joining(", "));  
//
//	    if (parameters.isEmpty()) {
//	        sb.append("Request parameters: NONE.\n");
//	    } else {
//	        sb.append("Request parameters: [" + parameters + "].\n");
//	    }
//	    
//		System.out.println("\n\n" + dtf.format(now) + " : " + sb + "\n");

		if (!hasAuthorizationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = getAccessToken(request);

		if (!jwtTokenUtility.validateAccessToken(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

		setAuthenticationContext(accessToken, request);
		filterChain.doFilter(request, response);
	}

	private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(accessToken);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				null);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private UserDetails getUserDetails(String accessToken) {
		User userDetails = new User();

		String[] subjectArray = jwtTokenUtility.getSubject(accessToken).split(",");
		userDetails.setUsername(subjectArray[0]);
		userDetails.setEmail(subjectArray[1]);
		return userDetails;
	}

	private boolean hasAuthorizationHeader(HttpServletRequest req) {
		
		String header = req.getHeader("Authorization");

		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
			return false;
		}

		return true;
	}

	private String getAccessToken(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		return header.split(" ")[1].trim();
	}
}
