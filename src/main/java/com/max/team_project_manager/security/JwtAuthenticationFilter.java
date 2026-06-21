package com.max.team_project_manager.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.max.team_project_manager.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final SecurityUserDetailsService securityUserDetailsService;

	public JwtAuthenticationFilter(
			JwtService jwtService,
			SecurityUserDetailsService securityUserDetailsService
	) {
		this.jwtService = jwtService;
		this.securityUserDetailsService = securityUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		if (SecurityContextHolder
				.getContext()
				.getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}

		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader == null ||
				!authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = authorizationHeader.substring(7);

		String username = jwtService.extractSubject(jwt);

		if (!jwtService.isTokenValid(jwt)) {
			filterChain.doFilter(request, response);
			return;
		}

		UserDetails userDetails = securityUserDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities());

		SecurityContextHolder.getContext()
			.setAuthentication(token);

		filterChain.doFilter(request, response);
		return;
	}
}
