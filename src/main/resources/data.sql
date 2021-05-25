DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS book_table;
DROP TABLE IF EXISTS role_table;
DROP TABLE IF EXISTS user_table;

CREATE TABLE user_table (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) DEFAULT NULL,
    name VARCHAR(50) DEFAULT NULL,
    password VARCHAR(225) DEFAULT NULL,
    mobile_no VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE book_table (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    book_title VARCHAR(50) DEFAULT NULL,
    author VARCHAR(50) DEFAULT NULL,
    book_status VARCHAR(10) DEFAULT NULL,
    username VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE role_table (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT(20) UNSIGNED,
    role_id INT(20) UNSIGNED,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES user_table (id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role_table (id)
);

INSERT INTO user_table (username, name, mobile_no, password) VALUES
('bzloo1', 'LibrarianName1', '0000000000','$2a$10$HG10MSCx8IY7RF6Nln5zpOotPk9gN6IJVMo8mG2QsBhW17uh8mPtW'),
('member', 'memberName1', '1111111111','$2a$10$HG10MSCx8IY7RF6Nln5zpOotPk9gN6IJVMo8mG2QsBhW17uh8mPtW');

INSERT INTO role_table ( role_name) VALUES
('ROLE_LIBRARIAN'),
('ROLE_MEMBER');

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2);

INSERT INTO book_table (book_title, author, username, book_status) VALUES
('Book 1', 'author 1', 'NONE','AVAILABLE'),
('Book 2', 'author 2', 'NONE','AVAILABLE'),
('Book 3', 'author 3', 'NONE','AVAILABLE'),
('Book 4', 'author 4', 'member','BORROWED');