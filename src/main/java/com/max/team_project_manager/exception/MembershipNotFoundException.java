package com.max.team_project_manager.exception;

public class MembershipNotFoundException extends RuntimeException {

    public MembershipNotFoundException(Long projectId, Long userId) {
        super("Membership not found for project " +
			   projectId + " and user " +
			   userId);
    }
}
