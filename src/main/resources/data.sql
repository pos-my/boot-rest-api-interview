INSERT INTO USER_TABLE (userName, userPassword, userRole)
VALUES('admin', '$2a$10$8YJIWVlAmCN5OWiclkVZruJ0lT9q5BXaIr2xXjuZ6se4m8p.Bkfxy', 'ADMIN');

INSERT INTO USER_TABLE (userId, userName, userPassword, userRole)
VALUES(2, 'james', '$2a$10$BtAQPm.O/YKhRdUCFJcF.OneAqa4bM91w1GEwDSr84pTzN7pRLW7e', 'LIBRARIAN');

INSERT INTO BOOK_TABLE (bookId, bookTitle, bookStatus)
VALUES(3, 'Harry Potter and the Prisoner of Azkaban', 'AVAILABLE');

INSERT INTO BOOK_TABLE (bookId, bookTitle, bookStatus)
VALUES(4, 'Harry Potter and the Goblet of Fire', 'AVAILABLE');

INSERT INTO BOOK_TABLE (bookId, bookTitle, bookStatus)
VALUES(5, 'Harry Potter and the Half Blood Prince', 'AVAILABLE');

INSERT INTO BOOK_TABLE (bookId, bookTitle, bookStatus)
VALUES(6, 'Harry Potter and the Order of Phoenix', 'BORROWED');
