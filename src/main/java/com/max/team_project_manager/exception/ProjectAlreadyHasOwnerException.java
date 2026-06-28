package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class ProjectAlreadyHasOwnerException extends AppException {

    public ProjectAlreadyHasOwnerException(Long projectId) {
        super(
				HttpStatus.CONFLICT,
				"PROJECT_ALREADY_HAS_OWNER",
				"Project with id " + projectId + " already has an owner"
		);
    }
}
