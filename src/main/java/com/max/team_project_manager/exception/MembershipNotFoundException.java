package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class MembershipNotFoundException extends AppException {

    public MembershipNotFoundException(Long projectId, Long userId) {
        super(
				HttpStatus.NOT_FOUND,
				"MEMBERSHIP_NOT_FOUND",
				"Membership not found for project " + projectId + 
				" and user " + userId
		);
    }
}
