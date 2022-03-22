DROP TABLE IF EXISTS user_tab;
DROP TABLE IF EXISTS book_tab;

CREATE TABLE `user_tab` (
    ut_id VARCHAR(255) NULL DEFAULT NULL,
    ut_username VARCHAR(255) NULL DEFAULT NULL,
    ut_email VARCHAR(255) NULL DEFAULT NULL,
    ut_password VARCHAR(255) NULL DEFAULT NULL,
    ut_role VARCHAR(255) NULL DEFAULT NULL,
    ut_status VARCHAR(255) NULL DEFAULT NULL,
    ut_record_create_date DATETIME NULL,
    ut_record_update_date DATETIME NULL,
    PRIMARY KEY (ut_id)
);

CREATE TABLE book_tab (
    bt_id VARCHAR(255) NULL DEFAULT NULL,
    bt_title VARCHAR(255) NULL DEFAULT NULL,
    bt_genre VARCHAR(255) NULL DEFAULT NULL,
    bt_status VARCHAR(255) NULL DEFAULT NULL,
    bt_record_create_date DATETIME NULL,
    bt_record_update_date DATETIME NULL,
    PRIMARY KEY (bt_id)
);