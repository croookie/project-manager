package com.max.team_project_manager.security;

import org.springframework.stereotype.Component;

@Component
public class DummyUserProvider implements CurrentUserProvider {

	@Override
	public Long getUserId() {
		return 51L;
	}
}
