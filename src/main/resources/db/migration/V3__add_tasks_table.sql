CREATE SEQUENCE tasks_seq
	INCREMENT BY 50;

CREATE TABLE tasks (
	id BIGINT DEFAULT nextval('tasks_seq') PRIMARY KEY,
	project_id BIGINT NOT NULL,
	membership_id BIGINT,
	title VARCHAR(100) NOT NULL,
	description VARCHAR(1000),
	status VARCHAR(20) NOT NULL,

	CONSTRAINT fk_tasks_project_id
		FOREIGN KEY (project_id)
		REFERENCES projects(id)
		ON DELETE CASCADE,

	CONSTRAINT fk_tasks_membership_id
		FOREIGN KEY (membership_id)
		REFERENCES memberships(id)
		ON DELETE SET NULL,

	CONSTRAINT ck_tasks_status
		CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE'))
);
