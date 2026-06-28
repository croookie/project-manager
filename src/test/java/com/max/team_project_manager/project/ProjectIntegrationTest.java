package com.max.team_project_manager.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.max.team_project_manager.auth.RegisterRequest;
import com.max.team_project_manager.common.IntegrationTestHelpers;
import com.max.team_project_manager.membership.ProjectMembership;
import com.max.team_project_manager.membership.ProjectMembershipRepository;
import com.max.team_project_manager.membership.Role;
import com.max.team_project_manager.user.User;
import com.max.team_project_manager.user.UserRepository;

import tools.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectIntegrationTest {

	@Autowired private MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired private UserRepository userRepository;
	@Autowired private ProjectRepository projectRepository;
	@Autowired private ProjectMembershipRepository projectMembershipRepository;

	private IntegrationTestHelpers helpers;

	private static final String EMAIL = "test@example.com";
	private static final String PASSWORD = "password";

	@BeforeEach
	public void setup() throws Exception {
		helpers = new IntegrationTestHelpers(mockMvc, objectMapper);

		projectRepository.deleteAll();
		projectMembershipRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void givenValidToken_whenPostProjects_thenReturnsProjectResponse() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(
				EMAIL,
				PASSWORD,
				"Test User"
		);

		String token = helpers.extractToken(
				helpers.register(registerRequest)
		);

		String name = "project-" + UUID.randomUUID();
		String description = "desc-" + UUID.randomUUID();

		CreateProjectRequest createProjectRequest = new CreateProjectRequest(
				name,
				description
		);

		ResultActions response = mockMvc.perform(
				post("/projects")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(helpers.asJson(createProjectRequest))
		);

		Project project = projectRepository
			.findByName(name)
			.orElseThrow();

		User user = userRepository
			.findByEmail(EMAIL)
			.orElseThrow();

		ProjectMembership membership = projectMembershipRepository
			.findByProjectIdAndUserId(
					project.getId(),
					user.getId())
			.orElseThrow();

		response.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.description").value(description))
			.andExpect(jsonPath("$.ownerId").value(user.getId()))
			.andExpect(jsonPath("$.ownerDisplayName").value("Test User"));

		assertEquals(1, projectMembershipRepository.count());
		assertEquals(1, projectRepository.count());

		assertEquals(name, project.getName());
		assertEquals(description, project.getDescription());
		assertEquals(Role.OWNER, membership.getRole());
	}
}
