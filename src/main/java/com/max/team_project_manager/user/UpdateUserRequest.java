package com.max.team_project_manager.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
	@NotBlank
	@Size(min = 3, max = 50)
	String displayName
) {}
