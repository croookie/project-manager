package com.max.team_project_manager.exception;

public class InsufficientPermissionsException extends RuntimeException {

	public InsufficientPermissionsException(String action) {
		super("Insufficient permissions to " + action);
	}
}
