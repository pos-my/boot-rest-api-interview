DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `book`;

CREATE TABLE `user` (
   `user_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `user_name` varchar(255) DEFAULT NULL,
   `user_role` bigint(20) DEFAULT NULL,
   `user_password` varchar(255) DEFAULT NULL,
   `status` varchar(255) DEFAULT NULL,
   `record_create_date` datetime DEFAULT NULL,
   `record_update_date` datetime DEFAULT NULL
);

CREATE TABLE `book` (
     `book_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
     `name` varchar(255) DEFAULT NULL,
     `stock` bigint(20) DEFAULT NULL,
     `status` varchar(255) DEFAULT NULL,
     `description` varchar(255) DEFAULT NULL,
     `record_create_date` datetime DEFAULT NULL,
     `record_update_date` datetime DEFAULT NULL
);

INSERT INTO book (name, stock, status, description, record_create_date, record_update_date)
VALUES ('Atomic Habits', 1, 'AVAILABLE', 'Self Help', now(), now());

INSERT INTO book (name, stock, status, description, record_create_date, record_update_date)
VALUES ('Harry Potter and the Philosopher''s Stone', 1, 'AVAILABLE', 'Fiction', now(), now());
