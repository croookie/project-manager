package com.max.team_project_manager.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.max.team_project_manager.dto.AddProjectMemberRequest;
import com.max.team_project_manager.exception.InsufficientPermissionsException;
import com.max.team_project_manager.exception.MembershipAlreadyExistsException;
import com.max.team_project_manager.exception.MembershipNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class ProjectMembershipServiceTest {

	@Mock private ProjectMembershipRepository projectMembershipRepository;
	@Mock private CurrentUserProvider currentUserProvider;
	@Mock private ProjectRepository projectRepository;
	@Mock private UserRepository userRepository;
	@Mock private ProjectMembershipMapper projectMembershipMapper;

	@InjectMocks private ProjectMembershipService projectMembershipService;

	private static final Long PROJECT_ID = 1L;
	private static final Long ACTOR_ID = 11L;
	private static final Long TARGET_ID = 12L;

	// removeMember()

	@Test
	public void ownerCannotRemoveOwner() {

		ProjectMembership actorMembership = membership(Role.OWNER);
		ProjectMembership targetMembership = membership(Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void ownerCanRemoveAdmin() {

		ProjectMembership actorMembership = membership(Role.OWNER);
		ProjectMembership targetMembership = membership(Role.ADMIN);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void ownerCanRemoveMember() {

		ProjectMembership actorMembership = membership(Role.OWNER);
		ProjectMembership targetMembership = membership(Role.MEMBER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void adminCannotRemoveOwner() {

		ProjectMembership actorMembership = membership(Role.ADMIN);
		ProjectMembership targetMembership = membership(Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void adminCannotRemoveAdmin() {

		ProjectMembership actorMembership = membership(Role.ADMIN);
		ProjectMembership targetMembership = membership(Role.ADMIN);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void adminCanRemoveMember() {

		ProjectMembership actorMembership = membership(Role.ADMIN);
		ProjectMembership targetMembership = membership(Role.MEMBER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void memberCannotRemoveAnyone() {

		ProjectMembership actorMembership = membership(Role.MEMBER);
		ProjectMembership targetMembership = membership(Role.MEMBER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembership(targetMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void shouldThrowWhenActorMembershipNotFoundRemoveMember() {

		stubCurrentUserId();
		stubActorMembershipNotFound();

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void shouldThrowWhenTargetMembershipNotFound() {

		ProjectMembership actorMembership = membership(Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);
		stubTargetMembershipNotFound();

		assertThrows(
				MembershipNotFoundException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	// addMember()

	@Test
	public void shouldThrowWhenAddingOwner() {

		AddProjectMemberRequest request = addMemberRequest(Role.OWNER);

		assertThrows(
				ProjectAlreadyHasOwnerException.class,
				() -> projectMembershipService.addMember(PROJECT_ID, request)
		);
	}

	@Test
	public void ownerCanAddAdmin() {
		AddProjectMemberRequest request = addMemberRequest(Role.ADMIN);

		Project project = project();

		User actor = user(ACTOR_ID);
		User target = user(TARGET_ID);

		ProjectMembership actorMembership = membership(project, actor, Role.OWNER);
		ProjectMembership targetMembership = membership(project, target, Role.ADMIN);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		when(projectRepository.findById(PROJECT_ID))
			.thenReturn(Optional.of(project));

		when(userRepository.findById(TARGET_ID))
			.thenReturn(Optional.of(target));

		when(projectMembershipRepository
				.existsByProjectIdAndUserId(
					PROJECT_ID,
					TARGET_ID))
			.thenReturn(false);

		when(projectMembershipMapper.toEntity(
					project,
					target,
					Role.ADMIN
			))
			.thenReturn(targetMembership);

		projectMembershipService.addMember(PROJECT_ID, request);

		verify(projectMembershipRepository)
			.save(targetMembership);
	}

	@Test
	public void ownerCanAddMember() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);

		Project project = project();

		User actor = user(ACTOR_ID);
		User target = user(TARGET_ID);

		ProjectMembership actorMembership = membership(project, actor, Role.OWNER);
		ProjectMembership targetMembership = membership(project, target, Role.MEMBER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		when(projectRepository.findById(PROJECT_ID))
			.thenReturn(Optional.of(project));

		when(userRepository.findById(TARGET_ID))
			.thenReturn(Optional.of(target));

		when(projectMembershipRepository
				.existsByProjectIdAndUserId(
					PROJECT_ID,
					TARGET_ID))
			.thenReturn(false);

		when(projectMembershipMapper.toEntity(
					project,
					target,
					Role.MEMBER
			))
			.thenReturn(targetMembership);

		projectMembershipService.addMember(PROJECT_ID, request);

		verify(projectMembershipRepository)
			.save(targetMembership);
	}

	@Test
	public void adminCannotAddAnyone() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);
		Project project = project();
		User actor = user(ACTOR_ID);
		ProjectMembership actorMembership = membership(project, actor, Role.ADMIN);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.addMember(PROJECT_ID, request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	@Test
	public void memberCannotAddAnyone() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);
		Project project = project();
		User actor = user(ACTOR_ID);
		ProjectMembership actorMembership = membership(project, actor, Role.MEMBER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.addMember(PROJECT_ID, request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	@Test
	public void shouldThrowWhenActorMembershipNotFoundAddMember() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);

		stubCurrentUserId();
		stubActorMembershipNotFound();

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.addMember(
						PROJECT_ID,
						request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	@Test
	public void shouldThrowWhenProjectNotFound() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);

		ProjectMembership actorMembership = membership(Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		when(projectRepository.findById(PROJECT_ID))
			.thenReturn(Optional.empty());

		assertThrows(
				ProjectNotFoundException.class,
				() -> projectMembershipService.addMember(
						PROJECT_ID,
						request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	@Test
	public void shouldThrowWhenUserNotFound() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);

		Project project = project();
		ProjectMembership actorMembership = membership(Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		when(projectRepository.findById(PROJECT_ID))
			.thenReturn(Optional.of(project));

		when(userRepository.findById(TARGET_ID))
			.thenReturn(Optional.empty());

		assertThrows(
				UserNotFoundException.class,
				() -> projectMembershipService.addMember(
						PROJECT_ID,
						request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	@Test
	public void shouldThrowWhenMembershipAlreadyExists() {
		AddProjectMemberRequest request = addMemberRequest(Role.MEMBER);

		Project project = project();
		User actor = user(ACTOR_ID);
		User target = user(TARGET_ID);
		ProjectMembership actorMembership = membership(project, actor, Role.OWNER);

		stubCurrentUserId();
		stubActorMembership(actorMembership);

		when(projectRepository.findById(PROJECT_ID))
			.thenReturn(Optional.of(project));

		when(userRepository.findById(TARGET_ID))
			.thenReturn(Optional.of(target));

		when(projectMembershipRepository.existsByProjectIdAndUserId(
				PROJECT_ID,
				request.getUserId()))
			.thenReturn(true);

		assertThrows(
				MembershipAlreadyExistsException.class,
				() -> projectMembershipService.addMember(
						PROJECT_ID,
						request)
		);

		verify(projectMembershipRepository, never())
			.save(any(ProjectMembership.class));
	}

	// stubs
	
	private void stubCurrentUserId() {
		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);
	}

	private void stubActorMembership(ProjectMembership actorMembership) {
		when(projectMembershipRepository.findByProjectIdAndUserId(
				PROJECT_ID,
				ACTOR_ID))
			.thenReturn(Optional.of(actorMembership));
	}

	private void stubActorMembershipNotFound() {
		when(projectMembershipRepository.findByProjectIdAndUserId(
				PROJECT_ID,
				ACTOR_ID))
			.thenReturn(Optional.empty());
	}

	private void stubTargetMembership(ProjectMembership targetMembership) {
		when(projectMembershipRepository.findByProjectIdAndUserId(
				PROJECT_ID,
				TARGET_ID))
			.thenReturn(Optional.of(targetMembership));
	}

	private void stubTargetMembershipNotFound() {
		when(projectMembershipRepository.findByProjectIdAndUserId(
				PROJECT_ID,
				TARGET_ID))
			.thenReturn(Optional.empty());
	}

	// factories

	private ProjectMembership membership(Role role) {
		ProjectMembership membership = new ProjectMembership();
		membership.setRole(role);
		return membership;
	}

	private ProjectMembership membership(Project project, User user, Role role) {
		ProjectMembership membership = new ProjectMembership();

		membership.setProject(project);
		membership.setUser(user);
		membership.setRole(role);

		return membership;
	}

	private AddProjectMemberRequest addMemberRequest(Role role) {
		AddProjectMemberRequest request = new AddProjectMemberRequest();

		request.setUserId(TARGET_ID);
		request.setRole(role);

		return request;
	}

	private Project project() {
		Project project = new Project();
		project.setId(PROJECT_ID);
		return project;
	}

	private User user(Long id) {
		User user = new User();
		user.setId(id);
		return user;
	}
}
