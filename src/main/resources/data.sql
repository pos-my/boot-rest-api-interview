INSERT INTO user (username, password, role, state)
VALUES('member', '$2a$10$lUbO25LF3hDka7yCkVnxMOtSOMwFFNCabpyNrt3nwzravnJmah7IG', 'MEMBER', 'ACTIVE');

INSERT INTO user (username, password, role, state)
VALUES('librarian', '$2a$10$MBtNDBR.83hV6lUU99hX6eMNCFv/xPfsQdInvFQbmDg9ad9kNUAl.', 'LIBRARIAN', 'ACTIVE');

INSERT INTO book (title, status)
VALUES('Kingdom of Spades', 'AVAILABLE');

INSERT INTO book (title, status)
VALUES('Sign of the Artificial Puppet', 'AVAILABLE');

INSERT INTO book (title, status)
VALUES('Cloaked Prayer', 'AVAILABLE');

INSERT INTO book (title, status)
VALUES('The Buried Curtain', 'BORROWED');