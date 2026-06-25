package com.max.team_project_manager.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUserProvider implements CurrentUserProvider {

	@Override
	public Long getUserId() {
		Authentication authentication =
			SecurityContextHolder.getContext().getAuthentication();

		SecurityUser user = (SecurityUser) authentication.getPrincipal();

		return user.getId();
	}
}
