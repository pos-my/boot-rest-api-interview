DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS books;


CREATE TABLE books (
  book_id INT AUTO_INCREMENT  PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  publisher VARCHAR(250) NOT NULL,
  year_establish VARCHAR(50) DEFAULT NULL,
  status VARCHAR(50) DEFAULT NULL
);

INSERT INTO books (title, publisher, year_establish, status) VALUES
  ('X1', 'P1', '2010', 'AVAILABLE'),
  ('Y2', 'P2', '2013', 'AVAILABLE'),
  ('Z3', 'P3', '2019', 'AVAILABLE');



  CREATE TABLE member (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    book_id INT DEFAULT NULL
  );

  INSERT INTO member (name) VALUES
    ('AST'),
    ('PPO'),
    ('TUT');