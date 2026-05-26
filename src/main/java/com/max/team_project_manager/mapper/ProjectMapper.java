package com.max.team_project_manager.mapper;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.dto.CreateProjectRequest;
import com.max.team_project_manager.dto.ProjectResponse;
import com.max.team_project_manager.model.Project;
import com.max.team_project_manager.model.User;

@Component
public class ProjectMapper {
	public Project toEntity(CreateProjectRequest request) {
		Project project = new Project();

		project.setName(request.getName());
		project.setDescription(request.getDescription());

		return project;
	}

	public ProjectResponse toResponse(Project savedProject, User user) {
		ProjectResponse response = new ProjectResponse();

		response.setId(savedProject.getId());
		response.setName(savedProject.getName());
		response.setDescription(savedProject.getDescription());
		response.setOwnerId(user.getId());
		response.setOwnerDisplayName(user.getDisplayName());

		return response;
	}
}
