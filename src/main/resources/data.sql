DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) DEFAULT NULL,
    password VARCHAR(225) DEFAULT NULL,
    first_name VARCHAR(50) DEFAULT NULL,
    last_name VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE book (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) DEFAULT NULL,
    author VARCHAR(50) DEFAULT NULL,
    status VARCHAR(10) DEFAULT NULL,
    user_id INT(20) UNSIGNED,
    PRIMARY KEY (id),
    CONSTRAINT fk_book_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE role (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) DEFAULT NULL,
    description VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
    id INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT(20) UNSIGNED,
    role_id INT(20) UNSIGNED,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role (id)
);

INSERT INTO user (username, password, first_name, last_name) VALUES
('librarian', '$2a$10$9SuBzx9Zy1Y//50sFvWqvuE9oX3lrzuLwUaiOFh8kl2WajIdw6V4S', 'firstname', 'lastname'),
('member', '$2a$10$9SuBzx9Zy1Y//50sFvWqvuE9oX3lrzuLwUaiOFh8kl2WajIdw6V4S', 'firstname', 'lastname');

INSERT INTO book (title, author, status, user_id) VALUES
('Book 1', 'author 1', 'AVAILABLE', null),
('Book 2', 'author 2', 'AVAILABLE', null),
('Book 3', 'author 3', 'AVAILABLE', null);

INSERT INTO role (name, description) VALUES
('ROLE_LIBRARIAN', 'Librarian'),
('ROLE_MEMBER', 'Member');

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2);