INSERT INTO USERS (ID, LOGINID, NAME, PASS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('u1', 'admin', 'Administrator', '$2a$04$HWICTb02ToiZRMVjtb/HQu.py3rWQxmswLO7u0XmI3uIL2rSCMlIK', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO USERS (ID, LOGINID, NAME, PASS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('u2', 'librarian1', 'Librarian 1', '$2a$04$HWICTb02ToiZRMVjtb/HQu.py3rWQxmswLO7u0XmI3uIL2rSCMlIK', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO USERS (ID, LOGINID, NAME, PASS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('u3', 'librarian2', 'Librarian 2', '$2a$04$HWICTb02ToiZRMVjtb/HQu.py3rWQxmswLO7u0XmI3uIL2rSCMlIK', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO USERS (ID, LOGINID, NAME, PASS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('u4', 'member1', 'Member 1', '$2a$04$HWICTb02ToiZRMVjtb/HQu.py3rWQxmswLO7u0XmI3uIL2rSCMlIK', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO USERS (ID, LOGINID, NAME, PASS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('u5', 'member2', 'Member 2', '$2a$04$HWICTb02ToiZRMVjtb/HQu.py3rWQxmswLO7u0XmI3uIL2rSCMlIK', 'SYSTEM', CURRENT_TIMESTAMP, 0);

INSERT INTO BOOKS (ID, NAME, STATUS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('b1', 'Book 1', 'AVAILABLE', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO BOOKS (ID, NAME, STATUS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('b2', 'Book 2', 'AVAILABLE', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO BOOKS (ID, NAME, STATUS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('b3', 'Book 3', 'AVAILABLE', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO BOOKS (ID, NAME, STATUS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('b4', 'Book 4', 'BORROWED', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO BOOKS (ID, NAME, STATUS, CREATED_BY, CREATED_DATE, VERSION) VALUES ('b5', 'Book 5', 'BORROWED', 'SYSTEM', CURRENT_TIMESTAMP, 0);

INSERT INTO ROLES (ID, NAME, CREATED_BY, CREATED_DATE, VERSION) VALUES ('admin', 'ADMIN', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO ROLES (ID, NAME, CREATED_BY, CREATED_DATE, VERSION) VALUES ('librarian', 'LIBRARIAN', 'SYSTEM', CURRENT_TIMESTAMP, 0);
INSERT INTO ROLES (ID, NAME, CREATED_BY, CREATED_DATE, VERSION) VALUES ('member', 'MEMBER', 'SYSTEM', CURRENT_TIMESTAMP, 0);

INSERT INTO AUTHORITIES (USERID, ROLEID) VALUES ('u1', 'admin');
INSERT INTO AUTHORITIES (USERID, ROLEID) VALUES ('u2', 'librarian');
INSERT INTO AUTHORITIES (USERID, ROLEID) VALUES ('u3', 'librarian');
INSERT INTO AUTHORITIES (USERID, ROLEID) VALUES ('u4', 'member');
INSERT INTO AUTHORITIES (USERID, ROLEID) VALUES ('u5', 'member');

INSERT INTO BORROWED_BOOKS (USERID, BOOKID) VALUES ('u4', 'b4');
INSERT INTO BORROWED_BOOKS (USERID, BOOKID) VALUES ('u4', 'b5');
