package com.max.team_project_manager.project;

import com.max.team_project_manager.membership.Role;

public record ProjectSummary(
	Long id,
	String name,
	Role role
) {}
