package com.max.team_project_manager.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
		@NotBlank
		@Size(min = 3, max = 100)
		String title,

		@Size(max = 1000)
		String description
) {}
