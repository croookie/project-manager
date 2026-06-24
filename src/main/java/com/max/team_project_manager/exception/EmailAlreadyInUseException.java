package com.max.team_project_manager.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
		super("Email " + email + " already in use");
    }
}
