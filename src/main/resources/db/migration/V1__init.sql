DROP TABLE IF EXISTS `TRANSACTION`;
DROP TABLE IF EXISTS `BOOK`;
DROP TABLE IF EXISTS `USER`;

CREATE TABLE `book` (
    `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `record_create_date` datetime DEFAULT NULL,
    `record_update_date` datetime DEFAULT NULL
);

CREATE TABLE `user` (
   `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `full_name` varchar(255) DEFAULT NULL,
   `username` varchar(255) DEFAULT NULL,
   `role` varchar(255) DEFAULT NULL,
   `password` varchar(255) DEFAULT NULL,
   `status` varchar(255) DEFAULT NULL,
   `record_create_date` datetime DEFAULT NULL,
   `record_update_date` datetime DEFAULT NULL
);

CREATE TABLE `transaction` (
    `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `book_id` int(11) DEFAULT NULL,
    `user_id` int(11) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL,
    `record_create_date` datetime DEFAULT NULL,
    `record_update_date` datetime DEFAULT NULL
);

INSERT INTO book (name, status, description, record_create_date, record_update_date)
VALUES ('Atomic Habits', 'AVAILABLE', 'Self Help', now(), now());

INSERT INTO book (name, status, description, record_create_date, record_update_date)
VALUES ('Harry Potter and the Philosopher''s Stone', 'AVAILABLE', 'Fiction', now(), now());
