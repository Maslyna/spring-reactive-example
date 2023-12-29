CREATE EXTENSION IF NOT EXISTS pgcrypto;


CREATE TABLE IF NOT EXISTS t_persons
(
    id                      UUID PRIMARY KEY,
    username                VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    role                    VARCHAR(50)  NOT NULL,
    account_non_expired     BOOLEAN      NOT NULL,
    account_non_locked      BOOLEAN      NOT NULL,
    credentials_non_expired BOOLEAN      NOT NULL,
    enabled                 BOOLEAN      NOT NULL
);
