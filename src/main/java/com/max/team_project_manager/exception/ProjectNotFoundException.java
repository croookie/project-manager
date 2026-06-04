package com.max.team_project_manager.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long projectId) {
        super("Project with id " + projectId + " not found");
    }
}
