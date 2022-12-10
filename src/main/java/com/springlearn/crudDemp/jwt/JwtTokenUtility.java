package com.springlearn.crudDemp.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.springlearn.crudDemp.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtility {

	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtility.class);

	@Value("${app.jwt.scerete}")
	private String secreteKey;

	public String generateAccessToken(User user) {
		return Jwts.builder().setSubject(user.getUsername() + "," + user.getEmail()).setIssuer("SHQ-Blog-App")
				// .setClaims(null)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, secreteKey).compact();

	}

	public boolean validateAccessToken(String token) {
		try {

			Jwts.parser().setSigningKey(secreteKey).parseClaimsJws(token);

			return true;

		} catch (ExpiredJwtException e) {
			logger.error("JWT Expired", e);
		} catch (IllegalArgumentException e) {
			logger.error("Token is null, empty or has only white space", e);
		} catch (MalformedJwtException e) {
			logger.error("JWT is invalid", e);
		} catch (UnsupportedJwtException e) {
			logger.error("JWT is not supported", e);
			System.out.println("======>>>>>");
		} catch (SignatureException e) {
			logger.error("Signature validation failed", e);
		}
		return false;
	}

	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser().setSigningKey(secreteKey).parseClaimsJws(token).getBody();
	}
}
