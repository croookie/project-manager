package com.max.team_project_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.dto.CreateProjectRequest;
import com.max.team_project_manager.dto.ProjectResponse;
import com.max.team_project_manager.dto.ProjectSummary;
import com.max.team_project_manager.mapper.ProjectMapper;
import com.max.team_project_manager.model.Project;
import com.max.team_project_manager.model.ProjectMembership;
import com.max.team_project_manager.model.Role;
import com.max.team_project_manager.model.User;
import com.max.team_project_manager.repository.ProjectMembershipRepository;
import com.max.team_project_manager.repository.ProjectRepository;
import com.max.team_project_manager.repository.UserRepository;
import com.max.team_project_manager.security.CurrentUserProvider;


@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;
	private final CurrentUserProvider currentUserProvider;
	private final UserRepository userRepository;
	private final ProjectMembershipRepository projectMembershipRepository;

	public ProjectService(
			ProjectRepository projectRepository,
			ProjectMapper projectMapper,
			CurrentUserProvider currentUserProvider,
			UserRepository userRepository,
			ProjectMembershipRepository projectMembershipRepository
	) {
		this.projectRepository = projectRepository;
		this.projectMapper = projectMapper;
		this.currentUserProvider = currentUserProvider;
		this.userRepository = userRepository;
		this.projectMembershipRepository = projectMembershipRepository;
	}

	@Transactional
	public ProjectResponse createProject(CreateProjectRequest request) {

		Project project = projectMapper.toEntity(request);
		Project savedProject = projectRepository.save(project);

		Long userId = currentUserProvider.getUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found!"));

		ProjectMembership membership = new ProjectMembership();
		membership.setProject(savedProject);
		membership.setUser(user);
		membership.setRole(Role.OWNER);
		projectMembershipRepository.save(membership);

		ProjectResponse response = projectMapper.toResponse(savedProject, user);
		
		return response;
	}

	public List<ProjectSummary> getCurrentUserProjects() {
		return projectMembershipRepository.findProjectsByUserId(
				currentUserProvider.getUserId()
		);
	}

	public Optional<ProjectResponse> getProjectById(Long projectId) {
		return projectRepository
				.findAccessibleById(projectId, currentUserProvider.getUserId());

	}

}
