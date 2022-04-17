insert into member(full_name, status, created_date) values ('Sarah', 'A', CURRENT_TIMESTAMP);
insert into member(full_name, status, created_date) values ('Ben', 'A', CURRENT_TIMESTAMP);
insert into member(full_name, status, created_date) values ('Kenny', 'A', CURRENT_TIMESTAMP);

insert into book(title, description, status, remarks, borrower_name, created_date) values ('Gone with The Wind', 'Old english literature', 'AVAILABLE', 'hardcopy', null, CURRENT_TIMESTAMP);
insert into book(title, description, status, remarks, borrower_name, created_date) values ('Alice in Wonderland', 'Children storybook', 'AVAILABLE', null, null, CURRENT_TIMESTAMP);
insert into book(title, description, status, remarks, borrower_name, created_date) values ('No Longer Human', 'Old japanese literature', 'BORROWED', null, 'Kenny', CURRENT_TIMESTAMP);
