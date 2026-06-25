package com.max.team_project_manager.membership;

import jakarta.validation.constraints.NotNull;

public record AddProjectMemberRequest(
	@NotNull
	Long userId,

	@NotNull
	Role role
) {}
