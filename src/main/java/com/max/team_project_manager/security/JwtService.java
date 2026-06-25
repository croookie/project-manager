package com.max.team_project_manager.security;

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

	public boolean isTokenValid(Claims claims) {
		String username = claims.getSubject();
		Date exp =  claims.getExpiration();

		return username != null
			&& !username.isBlank()
			&& exp != null
			&& exp.after(new Date());
	}

	public Claims extractClaims(String jwt) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
	}
}
