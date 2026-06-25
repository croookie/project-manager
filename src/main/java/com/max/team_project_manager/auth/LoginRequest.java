package com.max.team_project_manager.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest (
	@NotBlank
	@Email
	String email,

	@NotBlank
	@Size(min = 8, max = 50)
	String rawPassword
) {}
