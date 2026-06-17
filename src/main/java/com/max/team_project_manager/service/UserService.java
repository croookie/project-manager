package com.max.team_project_manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.dto.UpdateUserRequest;
import com.max.team_project_manager.dto.UserResponse;
import com.max.team_project_manager.exception.UserNotFoundException;
import com.max.team_project_manager.mapper.UserMapper;
import com.max.team_project_manager.model.User;
import com.max.team_project_manager.repository.UserRepository;
import com.max.team_project_manager.security.CurrentUserProvider;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final CurrentUserProvider currentUserProvider;

	public UserService(
			UserRepository userRepository,
			UserMapper userMapper,
			CurrentUserProvider currentUserProvider
	) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.currentUserProvider = currentUserProvider;
	}

	public UserResponse getById(Long id) {
		User user = userRepository
			.findById(id)
			.orElseThrow(() -> new UserNotFoundException(id));
		return userMapper.toResponse(user);
	}

	@Transactional
	public UserResponse updateProfile(UpdateUserRequest request) {
		Long currentUserId = currentUserProvider.getUserId();
		User user = userRepository
			.findById(currentUserId)
			.orElseThrow(() -> new UserNotFoundException(currentUserId));

		user.setDisplayName(request.getDisplayName());

		return userMapper.toResponse(user);
	}
}
