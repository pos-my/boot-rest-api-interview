DROP TABLE IF EXISTS librarian;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS book;


-- consider to merge?
CREATE TABLE librarian (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  token VARCHAR(250) NOT NULL
);

CREATE TABLE member (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  phone_number VARCHAR(250) DEFAULT NULL,
  token VARCHAR(250) NOT NULL
);

CREATE TABLE book (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  status VARCHAR(250) NOT NULL DEFAULT 'AVAILABLE'
);