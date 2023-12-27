CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE t_persons
(
    id        UUID NOT NULL DEFAULT gen_random_uuid(),
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    age       INT,
    CONSTRAINT pkey_persons PRIMARY KEY (id)
);