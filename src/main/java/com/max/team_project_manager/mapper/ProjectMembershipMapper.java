package com.max.team_project_manager.mapper;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.dto.ProjectMemberResponse;
import com.max.team_project_manager.model.Project;
import com.max.team_project_manager.model.ProjectMembership;
import com.max.team_project_manager.model.Role;
import com.max.team_project_manager.model.User;

@Component
public class ProjectMembershipMapper {

	public ProjectMembership toEntity(Project project, User user, Role role) {
		ProjectMembership pm = new ProjectMembership();

		pm.setProject(project);
		pm.setUser(user);
		pm.setRole(role);

		return pm;
	}

	public ProjectMemberResponse toResponse(ProjectMembership savedPm) {
		ProjectMemberResponse response = new ProjectMemberResponse();

		response.setUserId(savedPm.getUser().getId());
		response.setUserDisplayName(savedPm.getUser().getDisplayName());
		response.setRole(savedPm.getRole());

		return response;
	}
}
