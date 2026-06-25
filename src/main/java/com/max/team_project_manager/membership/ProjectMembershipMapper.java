package com.max.team_project_manager.membership;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.project.Project;
import com.max.team_project_manager.user.User;

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
		ProjectMemberResponse response = new ProjectMemberResponse(
				savedPm.getUser().getId(),
				savedPm.getUser().getDisplayName(),
				savedPm.getRole()
		);

		return response;
	}
}
