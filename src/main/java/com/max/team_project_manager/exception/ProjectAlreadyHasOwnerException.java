package com.max.team_project_manager.exception;

public class ProjectAlreadyHasOwnerException extends RuntimeException {

    public ProjectAlreadyHasOwnerException(Long projectId) {
        super("Project with id " + projectId + " already has an owner");
    }
}
