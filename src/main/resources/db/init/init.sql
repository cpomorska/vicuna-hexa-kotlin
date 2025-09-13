--liquibase formatted sql

--changeset init:db:first
DROP TABLE IF EXISTS public.errorevent;
DROP TABLE IF EXISTS public.userevent;
DROP TABLE IF EXISTS public.usertype CASCADE;
DROP TABLE IF EXISTS public.usernumer CASCADE;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.contact_info;

CREATE SEQUENCE IF NOT EXISTS public.userevent_seq start with 1 increment by 1;
CREATE SEQUENCE IF NOT EXISTS public.usertype_seq start with 1 increment by 1;
CREATE SEQUENCE IF NOT EXISTS public.usernumber_seq start with 1 increment by 1;
CREATE SEQUENCE IF NOT EXISTS public.user_seq start with 1 increment by 1;

CREATE TABLE IF NOT EXISTS public.userevent
(
    usereventid             bigserial    NOT NULL,
    usereventuuid           uuid NOT NULL,
    usertypedescription     varchar(255) NOT NULL,
    usereventtype           smallint NOT NULL,
    isremovable             BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_from            varchar(255) NULL,
    modified_at             timestamp(6) NULL,
    modified_from           varchar(255) NULL,
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT  ue_uuid UNIQUE (usereventuuid),
    CONSTRAINT  userevent_pkey PRIMARY KEY (usereventid)
);

CREATE TABLE IF NOT EXISTS public.usertype
(
    usertypeid              bigserial    NOT NULL,
    usertyperole            varchar(255) NOT NULL,
    usertypedescription     varchar(255) NOT NULL,
    created_at              timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_from            varchar(255) NULL,
    usertypeenabled         BOOLEAN NOT NULL,
    modified_at             timestamp(6) NULL,
    modified_from           varchar(255) NULL,
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_role UNIQUE (usertyperole),
    CONSTRAINT usertype_pkey PRIMARY KEY (usertypeid)
);

CREATE TABLE IF NOT EXISTS public.usernumber
(
    usernumberid        bigserial    NOT NULL,
    usernumber          uuid    NOT NULL,
    created_at          timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_from        varchar(255) NULL,
    modified_at         timestamp(6) NULL,
    modified_from       varchar(255) NULL,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_usernumber UNIQUE (usernumber),
    CONSTRAINT usernumber_pkey PRIMARY KEY (usernumberid)
);

CREATE TABLE IF NOT EXISTS public.users
(
    userid              bigserial    NOT NULL,
    usertypeid          bigserial    NOT NULL,
    usernumberid        bigserial    NOT NULL,
    username            varchar(255) NOT NULL,
    userdescription     varchar(255) NOT NULL,
    created_at          timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_from        varchar(255) NULL,
    modified_at         timestamp(6) NULL,
    modified_from       varchar(255) NULL,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    version             int NOT NULL DEFAULT 0,
    CONSTRAINT uq_name UNIQUE (username),
    CONSTRAINT user_pkey PRIMARY KEY (userid),
    CONSTRAINT user_type_fk FOREIGN KEY (usertypeid) REFERENCES public.usertype (usertypeid) ON DELETE RESTRICT,
    CONSTRAINT user_number_fk FOREIGN KEY (usernumberid) REFERENCES public.usernumber (usernumberid) ON DELETE RESTRICT
);

-- ContactInfo-Tabelle anlegen
CREATE SEQUENCE IF NOT EXISTS public.contact_info_seq start with 1 increment by 1;

CREATE TABLE IF NOT EXISTS public.contact_info
(
    contact_info_id    bigserial    NOT NULL,
    email              varchar(255) NOT NULL,
    phone              varchar(20),
    userid             bigserial,
    created_at          timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_from        varchar(255) NULL,
    modified_at         timestamp(6) NULL,
    modified_from       varchar(255) NULL,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    version             int NOT NULL DEFAULT 0,
    CONSTRAINT contact_info_pkey PRIMARY KEY (contact_info_id),
    CONSTRAINT fk_contact_user FOREIGN KEY (userid) REFERENCES public.users (userid) ON DELETE SET NULL
);

--changeset init:db:insert_usertypes_and_users

-- Insert user types (admin, user, nobody) falls nicht vorhanden
INSERT INTO public.usertype (usertyperole, usertypedescription, created_from, usertypeenabled, enabled) VALUES ('admin', 'Administrator', 'init.sql', TRUE, TRUE);
INSERT INTO public.usertype (usertyperole, usertypedescription, created_from, usertypeenabled, enabled) VALUES ('nobody', 'Nobody User', 'init.sql', TRUE, TRUE);
INSERT INTO public.usertype (usertyperole, usertypedescription, created_from, usertypeenabled, enabled) VALUES ('user', 'Standard User', 'init.sql', TRUE, TRUE);

-- Insert usernumbers (UUIDs fest vergeben)
INSERT INTO public.usernumber (usernumber, created_from, enabled) VALUES ('11111111-1111-1111-1111-111111111111', 'init.sql', TRUE);
INSERT INTO public.usernumber (usernumber, created_from, enabled) VALUES ('22222222-2222-2222-2222-222222222222', 'init.sql', TRUE);
INSERT INTO public.usernumber (usernumber, created_from, enabled) VALUES ('33333333-3333-3333-3333-333333333333', 'init.sql', TRUE);
INSERT INTO public.usernumber (usernumber, created_from, enabled) VALUES ('44444444-4444-4444-4444-444444444444', 'init.sql', TRUE);

-- Insert users (admin, allic, bob, nobody) falls nicht vorhanden
INSERT INTO public.users (usertypeid, usernumberid, username, userdescription, created_from, enabled, version)
SELECT ut.usertypeid, un.usernumberid, 'admin', 'Administrator', 'init.sql', TRUE, 0
FROM public.usertype ut, public.usernumber un
WHERE ut.usertyperole = 'admin' AND un.usernumber = '33333333-3333-3333-3333-333333333333'
  AND NOT EXISTS (SELECT 1 FROM public.users WHERE username = 'admin');

INSERT INTO public.users (usertypeid, usernumberid, username, userdescription, created_from, enabled, version)
SELECT ut.usertypeid, un.usernumberid, 'allic', 'User Allic', 'init.sql', TRUE, 0
FROM public.usertype ut, public.usernumber un
WHERE ut.usertyperole = 'user' AND un.usernumber = '11111111-1111-1111-1111-111111111111'
  AND NOT EXISTS (SELECT 1 FROM public.users WHERE username = 'allic');

INSERT INTO public.users (usertypeid, usernumberid, username, userdescription, created_from, enabled, version)
SELECT ut.usertypeid, un.usernumberid, 'bob', 'User Bob', 'init.sql', TRUE, 0
FROM public.usertype ut, public.usernumber un
WHERE ut.usertyperole = 'user' AND un.usernumber = '22222222-2222-2222-2222-222222222222'
  AND NOT EXISTS (SELECT 1 FROM public.users WHERE username = 'bob');

INSERT INTO public.users (usertypeid, usernumberid, username, userdescription, created_from, enabled, version)
SELECT ut.usertypeid, un.usernumberid, 'nobody', 'Nobody User', 'init.sql', TRUE, 0
FROM public.usertype ut, public.usernumber un
WHERE ut.usertyperole = 'nobody' AND un.usernumber = '44444444-4444-4444-4444-444444444444'
  AND NOT EXISTS (SELECT 1 FROM public.users WHERE username = 'nobody');

-- Beispiel-ContactInfo für admin, allic, bob, nobody
INSERT INTO public.contact_info (email, phone, userid)
SELECT 'admin@example.com', '+491234567892', u.userid
FROM public.users u WHERE u.username = 'admin'
  AND NOT EXISTS (SELECT 1 FROM public.contact_info WHERE email = 'admin@example.com');

INSERT INTO public.contact_info (email, phone, userid)
SELECT 'allic@example.com', '+491234567890', u.userid
FROM public.users u WHERE u.username = 'allic'
  AND NOT EXISTS (SELECT 1 FROM public.contact_info WHERE email = 'allic@example.com');

INSERT INTO public.contact_info (email, phone, userid)
SELECT 'bob@example.com', '+491234567891', u.userid
FROM public.users u WHERE u.username = 'bob'
  AND NOT EXISTS (SELECT 1 FROM public.contact_info WHERE email = 'bob@example.com');

INSERT INTO public.contact_info (email, phone, userid)
SELECT 'nobody@example.com', NULL, u.userid
FROM public.users u WHERE u.username = 'nobody'
  AND NOT EXISTS (SELECT 1 FROM public.contact_info WHERE email = 'nobody@example.com');

-- Ensure sequences are advanced beyond seeded data to avoid PK collisions during tests
ALTER SEQUENCE public.usertype_seq RESTART WITH 4;
ALTER SEQUENCE public.usernumber_seq RESTART WITH 5;
ALTER SEQUENCE public.user_seq RESTART WITH 5;
ALTER SEQUENCE public.contact_info_seq RESTART WITH 5;
