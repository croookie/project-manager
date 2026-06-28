package com.max.team_project_manager.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("""
	SELECT new com.max.team_project_manager.project.ProjectResponse(
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
		com.max.team_project_manager.membership.Role.OWNER
	""")
	Optional<ProjectResponse> findAccessibleById(Long projectId, Long userId);

	Optional<Project> findByName(String name);
}
