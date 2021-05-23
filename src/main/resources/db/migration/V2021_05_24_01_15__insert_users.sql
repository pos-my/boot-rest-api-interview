INSERT INTO users (username, password, enabled)
  values
  ('member', '$2y$12$QKEQfa4YaTPz.ciyWsw/0uZtXCbHEA.6Q.vaLshAKEi1dBtg3mBSG', 1),
  ('librarian', '$2y$12$d0aQzgBGKU7hHSKnD/5aau.nBrMNLYi1l4NFEX1dBWdUAekO9TXHu', 1);

INSERT INTO authorities (username, authority)
  values
  ('member', 'ROLE_MEMBER'),
  ('librarian', 'ROLE_LIBRARIAN');
