package com.max.team_project_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.max.team_project_manager.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
