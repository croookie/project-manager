package com.max.team_project_manager.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
		super("Email " + email + " already exists: ");
    }
}
