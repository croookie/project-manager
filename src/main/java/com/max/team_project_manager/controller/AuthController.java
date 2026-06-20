package com.max.team_project_manager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.team_project_manager.dto.AuthResponse;
import com.max.team_project_manager.dto.LoginRequest;
import com.max.team_project_manager.dto.RegisterRequest;
import com.max.team_project_manager.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(
			AuthService authService
	) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public AuthResponse register(
			@Valid
			@RequestBody
			RegisterRequest request
	) {
		return authService.register(request);
	}

	@PostMapping("/login")
	public AuthResponse login(
			@Valid
			@RequestBody
			LoginRequest request
	) {
		return authService.login(request);
	}
}
