package com.max.team_project_manager.membership;

import jakarta.validation.constraints.NotNull;

public class AddProjectMemberRequest {

	@NotNull
	private Long userId;

	@NotNull
	private Role role;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
