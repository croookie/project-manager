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
import com.max.team_project_manager.auth.LoginRequest;
import com.max.team_project_manager.auth.RegisterRequest;
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
		RegisterRequest request = new RegisterRequest(EMAIL, PASSWORD, "Test User");

		helpers.register(request)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void givenExistingEmail_whenRegister_thenReturnsConflict() throws Exception {
		RegisterRequest request = new RegisterRequest(EMAIL, PASSWORD, "Test User");

		helpers.register(request);

		helpers.register(request)
			.andDo(print())
			.andExpect(status().isConflict());
	}

	@Test
	public void givenValidCredentials_whenLogin_thenReturnsToken() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");
		helpers.register(registerRequest);

		LoginRequest loginRequest = new LoginRequest(EMAIL,PASSWORD);

		helpers.login(loginRequest)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void givenUnknownEmail_whenLogin_thenReturnsUnauthorized () throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");
		helpers.register(registerRequest);

		LoginRequest loginRequest = new LoginRequest("unknown@example.com", PASSWORD);

		helpers.login(loginRequest)
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.token").doesNotExist());
	}

	@Test
	public void givenInvalidPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");
		helpers.register(registerRequest);

		LoginRequest loginRequest = new LoginRequest(EMAIL, "invalid_password");

		helpers.login(loginRequest)
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.token").doesNotExist());
	}

	@Test
	public void givenValidToken_whenGetProjects_thenReturnsOk() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");

		String token = helpers.extractToken(
				helpers.register(registerRequest)
		);

		mockMvc.perform(
				get("/projects")
				.header("Authorization", "Bearer " + token)
		)
			.andExpect(status().isOk());
	}

	@Test
	public void givenMissingToken_whenGetProjects_thenReturnsUnauthorized() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");
		helpers.register(registerRequest);

		mockMvc.perform(
				get("/projects")
		)
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenInvalidToken_whenGetProjects_thenReturnsUnauthorized() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest(EMAIL, PASSWORD, "Test User");
		helpers.register(registerRequest);

		mockMvc.perform(
				get("/projects")
				.header("Authorization", "Bearer invalid-token")
		)
			.andExpect(status().isUnauthorized());
	}
}
