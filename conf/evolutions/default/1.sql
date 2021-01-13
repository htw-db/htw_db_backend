# --- !Ups

create table "person"
(
    "id"       BIGSERIAL PRIMARY KEY,
    "username" varchar not null
);

# --- !Downs

# --- !Ups

create table "instance"
(
    id        BIGSERIAL PRIMARY KEY,
    name      varchar   not null,
    person_id BIGSERIAL not null references person (id)
);

# --- !Downs