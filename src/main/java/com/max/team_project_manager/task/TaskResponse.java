package com.max.team_project_manager.task;

public record TaskResponse(
		Long id,
		Long projectId,
		String title,
		String description,
		Status status,
		Long assigneeId
) {}
