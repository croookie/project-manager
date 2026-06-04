package com.max.team_project_manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.dto.AddProjectMemberRequest;
import com.max.team_project_manager.dto.ProjectMemberResponse;
import com.max.team_project_manager.exception.InsufficientPermissionsException;
import com.max.team_project_manager.exception.MembershipAlreadyExistsException;
import com.max.team_project_manager.exception.ProjectAlreadyHasOwnerException;
import com.max.team_project_manager.exception.ProjectNotFoundException;
import com.max.team_project_manager.exception.UserNotFoundException;
import com.max.team_project_manager.mapper.ProjectMembershipMapper;
import com.max.team_project_manager.model.Project;
import com.max.team_project_manager.model.ProjectMembership;
import com.max.team_project_manager.model.Role;
import com.max.team_project_manager.model.User;
import com.max.team_project_manager.repository.ProjectMembershipRepository;
import com.max.team_project_manager.repository.ProjectRepository;
import com.max.team_project_manager.repository.UserRepository;
import com.max.team_project_manager.security.CurrentUserProvider;

@Service
public class ProjectMembershipService {

	private final ProjectMembershipRepository projectMembershipRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final ProjectMembershipMapper projectMembershipMapper;
	private final CurrentUserProvider currentUserProvider;

	public ProjectMembershipService(
			ProjectMembershipRepository projectMembershipRepository,
			ProjectRepository projectRepository,
			UserRepository userRepository,
			ProjectMembershipMapper projectMembershipMapper,
			CurrentUserProvider currentUserProvider
	) {
		this.projectMembershipRepository = projectMembershipRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.projectMembershipMapper = projectMembershipMapper;
		this.currentUserProvider = currentUserProvider;
	}

	@Transactional
	public ProjectMemberResponse addMember(
			Long projectId,
			AddProjectMemberRequest request
	) {
		if (request.getRole() == Role.OWNER) {
			throw new ProjectAlreadyHasOwnerException(projectId);
		}

		Project project = projectRepository
			.findById(projectId)
			.orElseThrow(() -> new ProjectNotFoundException(projectId));

		User user = userRepository
			.findById(request.getUserId())
			.orElseThrow(() -> new UserNotFoundException(request.getUserId()));

		projectMembershipRepository
			.findByProjectIdAndUserIdAndRole(
					projectId, 
					currentUserProvider.getUserId(),
					Role.OWNER)
			.orElseThrow(() -> new InsufficientPermissionsException("add project members"));


		if (projectMembershipRepository.existsByProjectIdAndUserId(projectId, request.getUserId())) {
					throw new MembershipAlreadyExistsException(
							projectId, 
							request.getUserId()
					);
		}

		ProjectMembership pm = projectMembershipMapper.toEntity(project, user, request.getRole());
		ProjectMembership savedPm = projectMembershipRepository.save(pm);

		return projectMembershipMapper.toResponse(savedPm);
	}
}
