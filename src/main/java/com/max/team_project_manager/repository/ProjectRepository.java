package com.max.team_project_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.max.team_project_manager.dto.ProjectResponse;
import com.max.team_project_manager.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("""
	SELECT new com.max.team_project_manager.dto.ProjectResponse(
		p.id,
		p.name,
		p.description,
		owner.user.id,
		owner.user.displayName
	)
	FROM Project p
	JOIN ProjectMembership access ON access.project = p
	JOIN ProjectMembership owner ON owner.project = p
	WHERE p.id = :projectId
	AND access.user.id = :userId
	AND owner.role =
		com.max.team_project_manager.model.Role.OWNER
	""")
	Optional<ProjectResponse> findAccessibleById(Long projectId, Long userId);
}
