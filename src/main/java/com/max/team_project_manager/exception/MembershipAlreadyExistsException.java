package com.max.team_project_manager.exception;

public class MembershipAlreadyExistsException extends RuntimeException {

    public MembershipAlreadyExistsException(Long projectId, Long userId) {
        super("User " + userId +
			  " is already a member of " + projectId);
    }
}
