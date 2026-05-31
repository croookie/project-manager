package com.max.team_project_manager.exception;

public class ProjectAlreadyHasOwnerException extends RuntimeException {

    public ProjectAlreadyHasOwnerException() {
        super("Project already has an owner");
    }
}
