INSERT INTO stocks_role (id, role)
VALUES (1, 'USER'), (0, 'ADMIN');

INSERT INTO stocks_user (id, username, email, password, register_date)
VALUES (0, 'antonadmin', 'anton@admin.com', '1234', CURRENT_TIMESTAMP());

INSERT INTO stocks_user_roles (roles_id, user_id)
VALUES (0, 0), (1, 0);
