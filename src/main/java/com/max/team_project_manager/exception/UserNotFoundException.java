package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {

    public UserNotFoundException(Long userId) {
        super(
				HttpStatus.NOT_FOUND,
				"USER_NOT_FOUND",
				"User with id " + userId + " not found"
		);
    }
}
