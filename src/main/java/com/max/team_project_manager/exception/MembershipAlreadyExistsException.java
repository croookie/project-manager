package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class MembershipAlreadyExistsException extends AppException {

    public MembershipAlreadyExistsException(Long projectId, Long userId) {
        super(
				HttpStatus.CONFLICT,
				"MEMBERSHIP_ALREADY_EXISTS",
				"User " + userId + " is already a member of " + projectId
		);
    }
}
