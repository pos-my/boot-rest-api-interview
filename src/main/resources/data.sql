insert into BOOKS (ID, CODE, TITLE, STATUS) values(1, 'BOOK001', 'Book 1 Title', 'AVAILABLE');
insert into BOOKS (ID, CODE, TITLE, STATUS) values(2, 'BOOK002', 'Book 2 Title', 'AVAILABLE');

insert into ROLES (ID, NAME) values(1, 'LIBRARIAN');
insert into ROLES (ID, NAME) values(2, 'MEMBER');

-- Login with username = alex, password = alex
insert into USERS (ID, NAME, USERNAME, PASSWORD, ENABLED) values(1, 'Alex',  'alex', '$2a$10$.tP2OH3dEG0zms7vek4ated5AiQ.EGkncii0OpCcGq4bckS9NOULu', 1);
-- Login with username = john, password = john
insert into USERS (ID, NAME, USERNAME, PASSWORD, ENABLED) values(2, 'John', 'john', '$2a$10$E2UPv7arXmp3q0LzVzCBNeb4B4AtbTAGjkefVDnSztOwE7Gix6kea', 1);

insert into USER_ROLES (USER_ID, ROLE_ID) values(1, 1);
insert into USER_ROLES (USER_ID, ROLE_ID) values(2, 2);