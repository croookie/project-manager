package com.max.team_project_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.team_project_manager.dto.CreateProjectRequest;
import com.max.team_project_manager.dto.ProjectResponse;
import com.max.team_project_manager.dto.ProjectSummary;
import com.max.team_project_manager.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping
	public ProjectResponse createProject(
			@RequestBody
			CreateProjectRequest request
	) {
		System.out.println("HIT CREATE USER ENDPOINT");
		return projectService.createProject(request);
	}

	/*
	@GetMapping
	public List<ProjectSummary> getAllProjects() {
		return projectService.getAllProjects();
	}
	*/
}
