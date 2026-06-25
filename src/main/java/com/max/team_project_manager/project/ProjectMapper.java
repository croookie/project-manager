package com.max.team_project_manager.project;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.user.User;

@Component
public class ProjectMapper {
	public Project toEntity(CreateProjectRequest request) {
		Project project = new Project();

		project.setName(request.name());
		project.setDescription(request.description());

		return project;
	}

	public ProjectResponse toResponse(Project savedProject, User user) {
		ProjectResponse response = new ProjectResponse(
				savedProject.getId(),
				savedProject.getName(),
				savedProject.getDescription(),
				user.getId(),
				user.getDisplayName()
		);

		return response;
	}
}
