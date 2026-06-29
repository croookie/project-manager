package com.max.team_project_manager.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import com.max.team_project_manager.common.IntegrationTestHelpers;
import com.max.team_project_manager.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JwtAuthenticationIntegrationTest {

	@Autowired private MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired private UserRepository userRepository;

	private IntegrationTestHelpers helpers;

	private static final String EMAIL = "test@example.com";
	private static final String PASSWORD = "password";

	@BeforeEach
	public void setup() {
		helpers = new IntegrationTestHelpers(mockMvc, objectMapper);

		userRepository.deleteAll();
	}

	@Test
	public void givenNewUser_whenRegister_thenReturnsToken() throws Exception {
		helpers.register(EMAIL, PASSWORD, "Test User")
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void givenExistingEmail_whenRegister_thenReturnsConflict() throws Exception {
		helpers.register(EMAIL, PASSWORD, "Test User");

		helpers.register(EMAIL, PASSWORD, "Test User")
			.andDo(print())
			.andExpect(status().isConflict());
	}

	@Test
	public void givenValidCredentials_whenLogin_thenReturnsToken() throws Exception {
		helpers.register(EMAIL, PASSWORD, "Test User");

		helpers.login(EMAIL,PASSWORD)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void givenUnknownEmail_whenLogin_thenReturnsUnauthorized () throws Exception {
		helpers.register(EMAIL, PASSWORD, "Test User");

		helpers.login("unknown@example.com", PASSWORD)
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.token").doesNotExist());
	}

	@Test
	public void givenInvalidPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
		helpers.register(EMAIL, PASSWORD, "Test User");

		helpers.login(EMAIL, "invalid_password")
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.token").doesNotExist());
	}

	@Test
	public void givenValidToken_whenGetProjects_thenReturnsOk() throws Exception {
		String token = helpers.registerAndGetToken(EMAIL, PASSWORD, "Test User");

		mockMvc.perform(
				get("/projects")
				.header("Authorization", "Bearer " + token)
		)
			.andExpect(status().isOk());
	}

	@Test
	public void givenMissingToken_whenGetProjects_thenReturnsUnauthorized() throws Exception {
		mockMvc.perform(get("/projects"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenInvalidToken_whenGetProjects_thenReturnsUnauthorized() throws Exception {
		mockMvc.perform(
				get("/projects")
				.header("Authorization", "Bearer invalid-token"))
			.andExpect(status().isUnauthorized());
	}
}
