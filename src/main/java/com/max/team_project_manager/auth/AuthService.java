package com.max.team_project_manager.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.max.team_project_manager.exception.EmailAlreadyInUseException;
import com.max.team_project_manager.security.JwtService;
import com.max.team_project_manager.user.User;
import com.max.team_project_manager.user.UserMapper;
import com.max.team_project_manager.user.UserRepository;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(
			UserRepository userRepository,
			UserMapper userMapper,
			AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder,
			JwtService jwtService
	) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyInUseException(request.getEmail());
		}

		User user = userMapper.toEntity(
				request, 
				passwordEncoder.encode(request.getRawPassword()));

		User saved = userRepository.save(user);

		String jwt = jwtService.generateToken(saved.getEmail());

		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(jwt);

		return authResponse;
	}

	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					request.getEmail(),
					request.getRawPassword()));

		String jwt = jwtService.generateToken(authentication.getName());

		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(jwt);

		return authResponse;
	}
}
