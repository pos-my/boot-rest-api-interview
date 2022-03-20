DROP TABLE if EXISTS users;
CREATE TABLE users (
    userid BIGINT PRIMARY KEY auto_increment,
    username VARCHAR2(128) UNIQUE,
    password VARCHAR2(256),
    role VARCHAR2(20),
    status VARCHAR2(20),
    createtime TIMESTAMP,
    updatetime TIMESTAMP
);

DROP TABLE if EXISTS books;
CREATE TABLE books (
    bookid BIGINT PRIMARY KEY auto_increment,
    bookname VARCHAR2(256),
    status VARCHAR2(20),
    createtime TIMESTAMP,
    updatetime TIMESTAMP
);
