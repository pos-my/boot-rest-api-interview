DROP TABLE IF EXISTS userdetail;
DROP TABLE IF EXISTS book;

CREATE TABLE userdetail (
    username varchar(255),
    age bigint,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    roles varchar(255),
	primary key(username)
);

CREATE TABLE book(
	book_id bigint,
    book_name varchar(255),
    status varchar(255),
    primary key(book_id)
    
);
