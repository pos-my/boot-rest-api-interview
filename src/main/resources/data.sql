INSERT INTO
    books (id, user_id, status, author, title, isnb)
VALUES
    (
        1,
        NULL,
        'AVAILABLE',
        'James Wan',
        'Despicable Me',
        '2222-3333-4444'
    ),
    (
        2,
        1,
        'BORROWED',
        'James Two',
        'Despicable You',
        '2222-3333-5555'
    );

INSERT INTO
    roles (id, name)
VALUES
    (1, 'MEMBER'),
    (2, 'LIBRARIAN');

INSERT INTO
    users (id, username, password, role_id)
VALUES
    (1, 'UserMember', 'User@1', 1),
    (2, 'UserLibrarian', 'User@2', 2),
    (3, 'UserMember3', 'User@3', 1);