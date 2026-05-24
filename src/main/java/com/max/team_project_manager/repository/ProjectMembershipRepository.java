package com.max.team_project_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.max.team_project_manager.model.ProjectMembership;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {
}
