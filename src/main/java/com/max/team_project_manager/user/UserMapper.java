package com.max.team_project_manager.user;

import org.springframework.stereotype.Component;

import com.max.team_project_manager.auth.RegisterRequest;

@Component
public class UserMapper {

	public UserResponse toResponse(User user) {
		UserResponse response = new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getDisplayName()
		);

		return response;
	}

	public User toEntity(RegisterRequest request, String passwordHash) {
		User user = new User();

		user.setEmail(request.email());
		user.setDisplayName(request.displayName());
		user.setPasswordHash(passwordHash);

		return user;
	}
}
