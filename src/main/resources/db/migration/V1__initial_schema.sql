CREATE SEQUENCE users_seq
	INCREMENT BY 50;

CREATE TABLE users (
	id BIGINT DEFAULT nextval('users_seq') PRIMARY KEY,
	email VARCHAR(255) NOT NULL UNIQUE,
	password_hash VARCHAR(60) NOT NULL,
	display_name VARCHAR(50) NOT NULL
);

CREATE SEQUENCE projects_seq
	INCREMENT BY 50;

CREATE TABLE projects (
	id BIGINT DEFAULT nextval('projects_seq') PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(1000) NOT NULL
);

CREATE SEQUENCE memberships_seq
	INCREMENT BY 50;

CREATE TABLE memberships (
	id BIGINT DEFAULT nextval('memberships_seq') PRIMARY KEY,
	project_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	role VARCHAR(255),

	CONSTRAINT fk_memberships_project_id
		FOREIGN KEY (project_id)
		REFERENCES projects(id),

	CONSTRAINT fk_memberships_user_id
		FOREIGN KEY (user_id)
		REFERENCES users(id),

	CONSTRAINT uq_memberships_project_id_user_id
		UNIQUE (project_id, user_id),

	CONSTRAINT ck_memberships_role
		CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER'))
);

CREATE UNIQUE INDEX uq_memberships_project_id_owner
	ON memberships(project_id)
	WHERE role = 'OWNER';
