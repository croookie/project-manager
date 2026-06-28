package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class InsufficientPermissionsException extends AppException {

	public InsufficientPermissionsException(String action) {
		super(
				HttpStatus.FORBIDDEN,
				"INSUFFICIENT_PERMISSIONS",
				"Insufficient permissions to " + action
		);
	}
}
