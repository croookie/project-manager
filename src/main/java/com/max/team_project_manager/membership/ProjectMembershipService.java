package com.max.team_project_manager.membership;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.exception.InsufficientPermissionsException;
import com.max.team_project_manager.exception.MembershipAlreadyExistsException;
import com.max.team_project_manager.exception.MembershipNotFoundException;
import com.max.team_project_manager.exception.ProjectAlreadyHasOwnerException;
import com.max.team_project_manager.exception.ProjectNotFoundException;
import com.max.team_project_manager.exception.UserNotFoundException;
import com.max.team_project_manager.project.Project;
import com.max.team_project_manager.project.ProjectRepository;
import com.max.team_project_manager.security.CurrentUserProvider;
import com.max.team_project_manager.user.User;
import com.max.team_project_manager.user.UserRepository;

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

		ProjectMembership actorMembership = projectMembershipRepository
			.findByProjectIdAndUserId(
					projectId, 
					currentUserProvider.getUserId()
			)
			.orElseThrow(() -> new InsufficientPermissionsException("add project members"));

		if (actorMembership.getRole() != Role.OWNER) {
			throw new InsufficientPermissionsException("add project members");
		}

		Project project = projectRepository
			.findById(projectId)
			.orElseThrow(() -> new ProjectNotFoundException(projectId));

		User user = userRepository
			.findById(request.getUserId())
			.orElseThrow(() -> new UserNotFoundException(request.getUserId()));

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

	@Transactional
	public void removeMember(Long projectId, Long userId) {

		ProjectMembership currentMember = projectMembershipRepository
			.findByProjectIdAndUserId(projectId, currentUserProvider.getUserId())
			.orElseThrow(() -> new InsufficientPermissionsException("remove project members"));

		ProjectMembership targetMember = projectMembershipRepository
			.findByProjectIdAndUserId(projectId, userId)
			.orElseThrow(() -> new MembershipNotFoundException(projectId, userId));

		Role actorRole = currentMember.getRole();
		Role targetRole = targetMember.getRole();

		if (actorRole != Role.OWNER &&
			actorRole != Role.ADMIN) {
			throw new InsufficientPermissionsException("remove project members");
		}

		if (targetRole == Role.OWNER) {
			throw new InsufficientPermissionsException("remove project owner");
		}

		if (actorRole != Role.OWNER &&
			targetRole == Role.ADMIN) {
			throw new InsufficientPermissionsException("remove project admins");
		}

		projectMembershipRepository.deleteByProjectIdAndUserId(projectId, userId);

	}
}
