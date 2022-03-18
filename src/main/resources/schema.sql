DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS refresh_token;

CREATE TABLE user (
    id bigint(15) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(9) NOT NULL,
    state VARCHAR(7) NOT NULL,
    created_time timestamp AS CURRENT_TIMESTAMP,
    updated_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE book (
    id bigint(15) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(9) NOT NULL,
    created_time timestamp AS CURRENT_TIMESTAMP,
    updated_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE refresh_token (
    id bigint(15) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id bigint(15) NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiry_time timestamp NOT NULL,
    created_time timestamp AS CURRENT_TIMESTAMP
);