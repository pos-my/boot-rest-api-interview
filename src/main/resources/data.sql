INSERT INTO role(role_id, name) VALUES (1,'LIBRARIAN');
INSERT INTO role(role_id, name) VALUES (2,'MEMBER');
INSERT INTO user(username, password, full_name, role_id, deleted, uuid) VALUES ('user1','$2a$12$eesbdMLEFyK.h01k8Q3OHeIw1Tq8zSja4jku5DF0djh0XtApWF3A6', 'John Doe', 1, 0,'e8162703-7cc5-421e-9d66-a5eabbed6f30');
INSERT INTO user(username, password, full_name, role_id, deleted, uuid) VALUES ('user2','$2a$12$ejqIjnEpEJUlDmIl200U4u/uPMn8L0nVdls3EO7MGe8bRPmR0OhrW', 'Will Smith', 2, 0,'aa0e12e7-3424-408e-a6a9-80b369dcbfc0');
INSERT INTO book(title, status, deleted) VALUES ('Oh, the Places You Go!', 'AVAILABLE', false);
INSERT INTO book(title, status, deleted) VALUES ('The Four Winds', 'AVAILABLE', false);