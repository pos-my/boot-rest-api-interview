CREATE TABLE users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
);

CREATE TABLE authorities (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE users_authorities (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  authorities_id bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (authorities_id) REFERENCES authorities(id)
);

CREATE UNIQUE INDEX ix_auth_username
  on authorities (username,authority);