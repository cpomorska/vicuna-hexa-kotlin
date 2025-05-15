--liquibase formatted sql

--changeset init:db:first
DROP TABLE IF EXISTS public.errorevent;
DROP TABLE IF EXISTS public.userevent;
DROP TABLE IF EXISTS public.usertype CASCADE;
DROP TABLE IF EXISTS public.usernumer CASCADE;
DROP TABLE IF EXISTS public.users;

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
    CONSTRAINT uq_name UNIQUE (username),
    CONSTRAINT user_pkey PRIMARY KEY (userid),
    CONSTRAINT user_type_fk FOREIGN KEY (usertypeid) REFERENCES public.usertype (usertypeid) ON DELETE SET NULL,
    CONSTRAINT user_number_fk FOREIGN KEY (usernumberid) REFERENCES public.usernumber (usernumberid) ON DELETE SET NULL
);


