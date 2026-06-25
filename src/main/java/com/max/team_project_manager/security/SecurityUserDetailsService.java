package com.max.team_project_manager.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.max.team_project_manager.user.User;
import com.max.team_project_manager.user.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public SecurityUserDetailsService(
			UserRepository userRepository
	) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return new SecurityUser(user);
	}
}
