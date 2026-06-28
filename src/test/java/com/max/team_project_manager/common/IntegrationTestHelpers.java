package com.max.team_project_manager.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;
import com.max.team_project_manager.auth.AuthResponse;
import com.max.team_project_manager.auth.LoginRequest;
import com.max.team_project_manager.auth.RegisterRequest;

public class IntegrationTestHelpers {

	private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public IntegrationTestHelpers(
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

	public ResultActions register(RegisterRequest request) throws Exception {
		return mockMvc.perform(
				post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		);
	}

	public ResultActions login(LoginRequest request) throws Exception {
		return mockMvc.perform(
				post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		);
	}

	public String extractToken(ResultActions resultActions) throws Exception {
		String body =  resultActions.andReturn()
			.getResponse()
			.getContentAsString();

		return objectMapper
			.readValue(body, AuthResponse.class)
			.token();
	}

	public String asJson(Object o) throws Exception {
		return objectMapper.writeValueAsString(o);
	}
}
