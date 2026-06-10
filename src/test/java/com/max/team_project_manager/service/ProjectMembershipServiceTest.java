package com.max.team_project_manager.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.max.team_project_manager.exception.InsufficientPermissionsException;
import com.max.team_project_manager.model.ProjectMembership;
import com.max.team_project_manager.model.Role;
import com.max.team_project_manager.repository.ProjectMembershipRepository;
import com.max.team_project_manager.security.CurrentUserProvider;

@ExtendWith(MockitoExtension.class)
public class ProjectMembershipServiceTest {

	@Mock
	private ProjectMembershipRepository projectMembershipRepository;
	@Mock
	private CurrentUserProvider currentUserProvider;

	@InjectMocks
	private ProjectMembershipService projectMembershipService;

	private static final Long PROJECT_ID = 1L;
	private static final Long ACTOR_ID = 11L;
	private static final Long TARGET_ID = 12L;

	@Test
	public void ownerCannotRemoveOwner() {

		ProjectMembership actor = membership(Role.OWNER);
		ProjectMembership target = membership(Role.OWNER);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void ownerCanRemoveAdmin() {

		ProjectMembership actor = membership(Role.OWNER);
		ProjectMembership target = membership(Role.ADMIN);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void ownerCanRemoveMember() {

		ProjectMembership actor = membership(Role.OWNER);
		ProjectMembership target = membership(Role.MEMBER);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void adminCannotRemoveOwner() {

		ProjectMembership actor = membership(Role.ADMIN);
		ProjectMembership target = membership(Role.OWNER);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void adminCannotRemoveAdmin() {

		ProjectMembership actor = membership(Role.ADMIN);
		ProjectMembership target = membership(Role.ADMIN);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}

	@Test
	public void adminCanRemoveMember() {

		ProjectMembership actor = membership(Role.ADMIN);
		ProjectMembership target = membership(Role.MEMBER);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		projectMembershipService.removeMember(PROJECT_ID, TARGET_ID);

		verify(projectMembershipRepository)
			.deleteByProjectIdAndUserId(PROJECT_ID, TARGET_ID);
	}

	@Test
	public void memberCannotRemoveAnyone() {

		ProjectMembership actor = membership(Role.MEMBER);
		ProjectMembership target = membership(Role.MEMBER);

		when(currentUserProvider.getUserId())
			.thenReturn(ACTOR_ID);

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, ACTOR_ID))
			.thenReturn(Optional.of(actor));

		when(projectMembershipRepository.findByProjectIdAndUserId(PROJECT_ID, TARGET_ID))
			.thenReturn(Optional.of(target));

		assertThrows(
				InsufficientPermissionsException.class,
				() -> projectMembershipService.removeMember(PROJECT_ID, TARGET_ID)
		);

		verify(projectMembershipRepository, never())
			.deleteByProjectIdAndUserId(anyLong(), anyLong());
	}
	
	//helper
	private ProjectMembership membership(Role role) {
		ProjectMembership membership = new ProjectMembership();
		membership.setRole(role);
		return membership;
	}
}
