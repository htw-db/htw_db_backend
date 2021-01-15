# --- !Ups

create table "person"
(
    "id"       BIGSERIAL PRIMARY KEY,
    "username" VARCHAR NOT NULL UNIQUE
);

# --- !Downs

# --- !Ups

create table "instance"
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR   NOT NULL UNIQUE,
    person_id BIGSERIAL not null references person (id)
);

# --- !Downs