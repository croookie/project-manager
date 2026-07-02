package com.max.team_project_manager.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.exception.InsufficientPermissionsException;
import com.max.team_project_manager.exception.MembershipNotFoundException;
import com.max.team_project_manager.exception.ProjectNotFoundException;
import com.max.team_project_manager.exception.UserNotFoundException;
import com.max.team_project_manager.membership.ProjectMembership;
import com.max.team_project_manager.membership.ProjectMembershipRepository;
import com.max.team_project_manager.membership.Role;
import com.max.team_project_manager.project.Project;
import com.max.team_project_manager.project.ProjectRepository;
import com.max.team_project_manager.security.CurrentUserProvider;
import com.max.team_project_manager.user.UserRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final ProjectMembershipRepository projectMembershipRepository;
	private final CurrentUserProvider currentUserProvider;

	public TaskService(
			TaskRepository taskRepository,
			ProjectRepository projectRepository,
			UserRepository userRepository,
			ProjectMembershipRepository projectMembershipRepository,
			CurrentUserProvider currentUserProvider
	) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.projectMembershipRepository = projectMembershipRepository;
		this.currentUserProvider = currentUserProvider;
	}

	@Transactional
	public TaskResponse createTask(Long projectId, CreateTaskRequest request) {
		Project project = projectRepository
			.findById(projectId)
			.orElseThrow(() -> new ProjectNotFoundException(projectId));

		Long actorId = currentUserProvider.getUserId();

		ProjectMembership membership = projectMembershipRepository
			.findByProjectIdAndUserId(projectId, actorId)
			.orElseThrow(() -> new MembershipNotFoundException(projectId, actorId));

		if (membership.getRole() != Role.OWNER &&
			membership.getRole() != Role.ADMIN) {
			throw new InsufficientPermissionsException("create task");
		}

		Task task = new Task();

		task.setProject(project);
		task.setTitle(request.title());
		task.setDescription(request.description());
		task.setStatus(Status.TODO);

		Task saved = taskRepository.save(task);

		return new TaskResponse(
				saved.getId(),
				saved.getProject().getId(),
				saved.getTitle(),
				saved.getDescription(),
				saved.getStatus(),
				saved.getAssignee() != null ? saved.getAssignee().getId() : null
		);
	}
}
