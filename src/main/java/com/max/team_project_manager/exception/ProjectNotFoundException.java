package com.max.team_project_manager.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends AppException {

    public ProjectNotFoundException(Long projectId) {
        super(
				HttpStatus.NOT_FOUND,
				"PROJECT_NOT_FOUND",
				"Project with id " + projectId + " not found"
		);
    }
}
