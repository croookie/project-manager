package com.max.team_project_manager.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.max.team_project_manager.common.ErrorResponse;
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

	private static final String EMAIL = "email@test.com";
	private static final String PASSWORD = "password";
	private static final String DISPLAY_NAME = "Test User";

	@BeforeEach
	public void setup() throws Exception {
		helpers = new IntegrationTestHelpers(mockMvc, objectMapper);

		projectMembershipRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
	}

	// POST projects

	@Test
	public void givenValidToken_whenPostProjects_thenReturnsProjectResponse() throws Exception {
		final String name = "Project";

		String token = helpers.registerAndGetToken(EMAIL, PASSWORD, DISPLAY_NAME);

		ResultActions response = helpers.postProjects(name, "Desc", token);

		Long projectId = objectMapper.readTree(response
				.andReturn()
				.getResponse()
				.getContentAsString())
			.get("id").asLong();

		Project project = projectRepository
			.findById(projectId)
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
			.andExpect(jsonPath("$.description").value("Desc"))
			.andExpect(jsonPath("$.ownerId").value(user.getId()))
			.andExpect(jsonPath("$.ownerDisplayName").value(DISPLAY_NAME));

		assertEquals(1, projectMembershipRepository.count());
		assertEquals(1, projectRepository.count());

		assertEquals(name, project.getName());
		assertEquals("Desc", project.getDescription());
		assertEquals(Role.OWNER, membership.getRole());
	}

	@Test
	public void givenBlankName_whenPostProjects_thenReturnsValidationError() throws Exception {
		String token = helpers.registerAndGetToken(EMAIL, PASSWORD, DISPLAY_NAME);

		String body = helpers.postProjects("", "", token)
			.andExpect(status().isBadRequest())
			.andReturn()
			.getResponse()
			.getContentAsString();

		ErrorResponse response =
			objectMapper.readValue(body, ErrorResponse.class);

		assertEquals("VALIDATION_ERROR", response.code());
		assertEquals(0, projectRepository.count());
		assertEquals(0, projectMembershipRepository.count());
	}

	// GET projects

	@Test
	public void givenOtherProjects_whenGetProjects_thenReturnsMyProjects () throws Exception {
		String myToken = helpers.registerAndGetToken("me@test.com", PASSWORD, "My User");
		String otherToken = helpers.registerAndGetToken("other@test.com", PASSWORD, "Other User");

		helpers.postProjects("My Project 1", "My Desc 1", myToken);
		helpers.postProjects("My Project 2", "My Desc 2", myToken);
		helpers.postProjects("Other Project", "Other Desc", otherToken);

		String body = helpers.getProjects(myToken)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<ProjectSummary> myProjects = Arrays.asList(
				objectMapper.readValue(body, ProjectSummary[].class));

		assertThat(myProjects).hasSize(2);
		assertThat(myProjects)
			.extracting(ProjectSummary::name, ProjectSummary::role)
			.containsExactlyInAnyOrder(
					tuple("My Project 1", Role.OWNER),
					tuple("My Project 2", Role.OWNER));
	}

	@Test
	public void givenNoProjects_whenGetProjects_thenReturnsEmptyList() throws Exception {
		String token = helpers.registerAndGetToken(EMAIL, PASSWORD, DISPLAY_NAME);

		String body = helpers.getProjects(token)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<ProjectSummary> projects = Arrays.asList(
				objectMapper.readValue(body, ProjectSummary[].class));

		assertThat(projects).isEmpty();
	}
}
