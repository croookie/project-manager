package com.max.team_project_manager.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.dto.CreateUserRequest;
import com.max.team_project_manager.dto.UserResponse;
import com.max.team_project_manager.exception.EmailAlreadyExistsException;
import com.max.team_project_manager.mapper.UserMapper;
import com.max.team_project_manager.model.User;
import com.max.team_project_manager.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public UserService(
			UserRepository userRepository,
			UserMapper userMapper,
			PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserResponse createUser(CreateUserRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException(request.getEmail());
		}

		String passwordHash = passwordEncoder.encode(request.getRawPassword());
		User user = userMapper.toEntity(request, passwordHash);

		User saved = userRepository.save(user);
		UserResponse response = userMapper.toResponse(saved);

		return response;
	}

}
