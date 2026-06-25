package com.max.team_project_manager.project;

public class ProjectResponse {
	private Long id;
	private String name;
	private String description;
	private Long ownerId;
	private String ownerDisplayName;

	public ProjectResponse () {}

	public ProjectResponse (
			Long id,
			String name,
			String description,
			Long ownerId,
			String ownerDisplayName
	) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.ownerId = ownerId;
		this.ownerDisplayName = ownerDisplayName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}
	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
	}
}
