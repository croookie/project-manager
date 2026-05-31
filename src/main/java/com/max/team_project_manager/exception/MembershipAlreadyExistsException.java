package com.max.team_project_manager.exception;

public class MembershipAlreadyExistsException extends RuntimeException {

    public MembershipAlreadyExistsException() {
        super("Membership already exists");
    }
}
