package com.max.team_project_manager.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.team_project_manager.dto.AddProjectMemberRequest;
import com.max.team_project_manager.dto.ProjectMemberResponse;
import com.max.team_project_manager.service.ProjectMembershipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects/{projectId}/members")
public class ProjectMembershipController {

	private final ProjectMembershipService projectMembershipService;

	public ProjectMembershipController(
			ProjectMembershipService projectMembershipService
	) {
		this.projectMembershipService = projectMembershipService;
	}

	@PostMapping
	public ProjectMemberResponse addMember(
			@PathVariable Long projectId,
			@Valid @RequestBody AddProjectMemberRequest request
	) {
		return projectMembershipService.addMember(projectId, request);
	}

	@DeleteMapping("/{userId}")
	public void removeMember(
			@PathVariable Long projectId,
			@PathVariable Long userId
	) {
		projectMembershipService.removeMember(projectId, userId);
	}
}
