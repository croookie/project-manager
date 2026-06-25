package com.max.team_project_manager.project;

public record ProjectResponse (
	Long id,
	String name,
	String description,
	Long ownerId,
	String ownerDisplayName
) {}
