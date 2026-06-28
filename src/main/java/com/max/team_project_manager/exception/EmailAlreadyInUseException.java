package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends AppException {

    public EmailAlreadyInUseException(String email) {
		super(
				HttpStatus.CONFLICT,
				"EMAIL_ALREADY_IN_USE",
				"Email " + email + " already in use"
		);
    }
}
