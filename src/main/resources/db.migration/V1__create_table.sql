-- Drop types if they exist
drop type if exists role cascade;

-- Drop sequences if they exist
drop sequence IF exists user_infos_seq cascade;
drop sequence IF exists users_seq cascade;

create type role as enum ('ADMIN', 'USER');

create table user_infos
(
    id       serial primary key,
    username varchar unique,
    password varchar,
    role     role
);

create table users
(
    id           serial primary key,
    phone_number varchar,
    image        varchar,
    user_info_id integer references user_infos (id)
);

alter table users
    add constraint fk_users_user_infos foreign key (user_info_id) references user_infos (id);