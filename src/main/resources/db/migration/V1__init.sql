
create table appuser
(
  id bigserial not null
    constraint appuser_pkey
    primary key,
  secret varchar(50) default 'mySecret'::character varying not null,
  email varchar(50) not null,
  enabled boolean not null,
  firstname varchar(50) not null,
  key varchar(50),
  lastpasswordresetdate timestamp not null,
  lastname varchar(50) not null,
  password varchar(100) not null,
  username varchar(50) not null
    constraint uk_418sr20kh207kb22viuyno1
    unique
);




create table authority
(
  id bigserial not null
    constraint authority_pkey
    primary key,
  name varchar(50) not null
);

create table user_authority
(
  user_id      BIGINT NOT NULL
    CONSTRAINT fkjk083dm06nfs1ycs8jeyjevdy
    REFERENCES appuser,
  authority_id BIGINT NOT NULL
    CONSTRAINT fkgvxjs381k6f48d5d2yi11uh89
    REFERENCES authority
);



INSERT INTO APPUSER (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE)
  VALUES
(nextval('appuser_id_seq'), 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 'admin', 'admin@admin.com', true, '01-01-2016'),
(nextval('appuser_id_seq'), 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'enabled@user.com', true, '01-01-2016'),
(nextval('appuser_id_seq'), 'disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'disabled@user.com', false, '01-01-2016');

INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
