# --- !Ups

create table "person" (
  "id" BIGSERIAL PRIMARY KEY,
  "username" varchar not null
);

# --- !Downs

drop table "person" if exists;