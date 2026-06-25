package com.max.team_project_manager.membership;

public record ProjectMemberResponse(
	Long userId,
	String userDisplayName,
	Role role
) {}
