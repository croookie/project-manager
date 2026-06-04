package com.max.team_project_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.max.team_project_manager.dto.ProjectSummary;
import com.max.team_project_manager.model.ProjectMembership;
import com.max.team_project_manager.model.Role;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {

	@Query("""
	SELECT new com.max.team_project_manager.dto.ProjectSummary(
		p.id,
		p.name,
		m.role
	)
	FROM ProjectMembership m
	JOIN m.project p
	WHERE m.user.id = :userId
	""")
	List<ProjectSummary> findProjectsByUserId(Long userId);

	Optional<ProjectMembership> findByProjectIdAndUserIdAndRole(Long projectId, Long userId, Role role);

	boolean existsByProjectIdAndUserId(Long projectId, Long userId);

	Optional<ProjectMembership> findByProjectIdAndUserId(Long projectId, Long userId);

	void deleteByProjectIdAndUserId(Long projectId, Long userId);
}
