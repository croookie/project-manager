package com.max.team_project_manager.project;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping
	public ProjectResponse createProject(
			@Valid
			@RequestBody
			CreateProjectRequest request
	) {
		return projectService.createProject(request);
	}

	@GetMapping
	public List<ProjectSummary> getCurrentUserProjects() {
		return projectService.getCurrentUserProjects();
	}

	@GetMapping("/{id}")
	public ProjectResponse getProject(
			@PathVariable Long id
	) {
		return projectService.getProjectById(id);
	}
}
