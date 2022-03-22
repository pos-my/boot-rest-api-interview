-- book_tab
INSERT INTO book_tab
    (bt_id, bt_title, bt_genre, bt_status, bt_record_create_date, bt_record_update_date)
VALUES
    ('9443f8032a43444fa2f2d21f08d7b1df', 'Horrors Of Baskville', 'Horror', 'AVAILABLE', now(), now()),
    ('2b4329cbf8534e349f625ff2574dd4f3', 'Sunshine', 'Drama', 'BORROWED', now(), now());

-- user_tab
INSERT INTO user_tab
    (ut_id, ut_username, ut_email, ut_password, ut_role, ut_status, ut_record_create_date, ut_record_update_date)
VALUES
    ('10ea06009d45423593065b72e0b52e73', 'SherlockHolmes', 'sherlock@holmes.com', '$2a$12$VZzMb6CgZjNAFSva4Xmk2OS4R4SNinH.vtpK1/J6J.BlDml9hi2ZK', 'LIBRARIAN', 'ACTIVE', now(), now()),
    ('ef29203e9c334af3aef55b0680eeaa5b', 'UserMember', 'member@holmes.com', '$2a$12$VZzMb6CgZjNAFSva4Xmk2OS4R4SNinH.vtpK1/J6J.BlDml9hi2ZK', 'MEMBER', 'ACTIVE', now(), now());