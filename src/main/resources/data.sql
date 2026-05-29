-- USERS
-- current user for access
-- user_id = 1
INSERT INTO users (
	id, email, password_hash, display_name
)
VALUES (
	nextval('users_seq'),
	'access@gmail.com',
	'six_seceeenn',
	'Should be used for access'
);

-- owner
-- user_id = 2
INSERT INTO users (
	id, email, password_hash, display_name
)
VALUES (
	nextval('users_seq'),
	'owner@gmail.com',
	'stupidpassword',
	'Should be owner'
);

-- junk
INSERT INTO users (
	id, email, password_hash, display_name
)
VALUES (
	nextval('users_seq'),
	'junk@gmail.com',
	'stupidpassword',
	'invalid'
);

-- PROJECTS
-- desired project
-- id = 1
INSERT INTO projects (
	id, name, description
)
VALUES (
	nextval('projects_seq'),
	'The Right One',
	'FINALLY DAWG OMG YAY'
);

-- junk
-- id = 2
INSERT INTO projects (
	id, name, description
)
VALUES (
	nextval('projects_seq'),
	'The Wrong One',
	'OH HELL NAW'
);
-- id = 3
INSERT INTO projects (
	id, name, description
)
VALUES (
	nextval('projects_seq'),
	'The Wrong One',
	'OH HELL NAW'
);

-- MEMBERSHIPS
-- id = 1; access
INSERT INTO memberships (
	id, project_id, user_id, role
)
VALUES (
	nextval('memberships_seq'),
	(SELECT id FROM projects WHERE name = 'The Right One'),
	(SELECT id FROM users WHERE email = 'access@gmail.com'),
	'MEMBER'
);
-- id = 2; ownership
INSERT INTO memberships (
	id, project_id, user_id, role
)
VALUES (
	nextval('memberships_seq'),
	(SELECT id FROM projects WHERE name = 'The Right One'),
	(SELECT id FROM users WHERE email = 'owner@gmail.com'),
	'OWNER'
);

-- junk
-- id = 3
INSERT INTO memberships (
	id, project_id, user_id, role
)
VALUES (
	nextval('memberships_seq'),
	1,
	101,
	'MEMBER'
);
-- id = 4
INSERT INTO memberships (
	id, project_id, user_id, role
)
VALUES (
	nextval('memberships_seq'),
	51,
	101,
	'OWNER'
);
