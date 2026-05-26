INSERT INTO users (
	id,
	email,
	password_hash,
	display_name
)
VALUES (
	nextval('users_seq'),
	'max@huu.com',
	'six_seceeenn',
	'Maksimka'
);
