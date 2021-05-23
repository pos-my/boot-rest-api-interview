CREATE TABLE `library`.`book` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255),
    `author` VARCHAR(255),
    `book_status` VARCHAR(255) NOT NULL,
    `creation_date` TIMESTAMP NOT NULL,
    `last_edit_date` TIMESTAMP,
    `borrow_by` VARCHAR(255),
    PRIMARY KEY (`id`)
)