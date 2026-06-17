package com.max.team_project_manager.mapper;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.dto.RegisterRequest;
import com.max.team_project_manager.dto.UserResponse;
import com.max.team_project_manager.model.User;

@Component
public class UserMapper {

	public UserResponse toResponse(User user) {
		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setDisplayName(user.getDisplayName());

		return response;
	}

	public User toEntity(RegisterRequest request, String passwordHash) {
		User user = new User();

		user.setEmail(request.getEmail());
		user.setDisplayName(request.getDisplayName());
		user.setPasswordHash(passwordHash);

		return user;
	}
}
