package com.max.team_project_manager.membership;

import com.max.team_project_manager.project.Project;
import com.max.team_project_manager.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "memberships",
	uniqueConstraints = @UniqueConstraint(
		columnNames = { "project_id", "user_id" }
	)
)
public class ProjectMembership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// Database constraint ensures no more than one OWNER per project
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Role role;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
