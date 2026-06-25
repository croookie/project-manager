package com.max.team_project_manager.project;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.exception.ProjectNotFoundException;
import com.max.team_project_manager.exception.UserNotFoundException;
import com.max.team_project_manager.membership.ProjectMembership;
import com.max.team_project_manager.membership.ProjectMembershipRepository;
import com.max.team_project_manager.membership.Role;
import com.max.team_project_manager.security.CurrentUserProvider;
import com.max.team_project_manager.user.User;
import com.max.team_project_manager.user.UserRepository;


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
			.orElseThrow(() -> new UserNotFoundException(userId));

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

	public ProjectResponse getProjectById(Long projectId) {
		return projectRepository
				.findAccessibleById(projectId, currentUserProvider.getUserId())
				.orElseThrow(() -> new ProjectNotFoundException(projectId));
	}

}
