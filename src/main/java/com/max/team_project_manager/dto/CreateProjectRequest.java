package com.max.team_project_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateProjectRequest {

	@NotBlank
	@Size(min = 3, max = 50)
	private String name;

	@Size(max = 1000)
	private String description;

	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}
