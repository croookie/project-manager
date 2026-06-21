package com.max.team_project_manager.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

	public String generateToken(String subject) {
		return Jwts.builder()
			.subject(subject)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis()
						+ 1000 * 60 * 10))
			.signWith(key)
			.compact();
	}

	public String extractSubject(String jwt) {
		return extractAllClaims(jwt).getSubject();
	}

	public boolean isTokenValid(String jwt) {
		return !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		return extractAllClaims(jwt)
			.getExpiration()
			.before(new Date());
	}

	private Claims extractAllClaims(String jwt) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
	}
}
