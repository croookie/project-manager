package com.max.team_project_manager.user;

public record UserResponse(
	Long id,
	String email,
	String displayName
) {}
