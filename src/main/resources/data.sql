DROP TABLE IF EXISTS books;

CREATE TABLE books (
  id INT PRIMARY KEY,
  name VARCHAR(250),
  bookStatus VARCHAR(250)
);

DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id INT PRIMARY KEY,
    name VARCHAR(250),
    role VARCHAR(250),
    roleStatus VARCHAR(250)
);