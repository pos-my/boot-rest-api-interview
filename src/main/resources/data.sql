INSERT INTO user_table (username, password) VALUES
('bzloo1', '$2a$10$HG10MSCx8IY7RF6Nln5zpOotPk9gN6IJVMo8mG2QsBhW17uh8mPtW'),
('member', '$2a$10$HG10MSCx8IY7RF6Nln5zpOotPk9gN6IJVMo8mG2QsBhW17uh8mPtW');

INSERT INTO role_table ( role_name) VALUES
('ROLE_LIBRARIAN'),
('ROLE_MEMBER');

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2);