package com.max.team_project_manager.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest (
	@NotBlank
	@Size(min = 3, max = 50)
	String name,

	@Size(max = 1000)
	String description
) {}
