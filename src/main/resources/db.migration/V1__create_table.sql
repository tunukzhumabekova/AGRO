-- Drop types if they exist
drop type if exists role cascade;
drop type if exists seasons cascade;
drop type if exists profit cascade;

-- Drop sequences if they exist
drop sequence IF exists user_infos_seq cascade;
drop sequence IF exists users_seq cascade;
drop sequence if exists category_seq cascade;
drop sequence if exists season_seq cascade;
drop sequence if exists recommendation_seq cascade;
drop sequence if exists direction_seq cascade;
drop sequence if exists record_seq cascade;
drop sequence if exists breed_seq cascade;
drop sequence if exists user_pets_seq cascade;
drop sequence if exists transactions_seq cascade;

drop table if exists user_infos cascade;
drop table if exists users cascade;
drop table if exists category cascade;
drop table if exists season cascade;
drop table if exists recommendation cascade;
drop table if exists direction cascade;
drop table if exists record cascade;
drop table if exists breed cascade;
drop table if exists user_pets cascade;
drop table if exists transactions cascade;

create type role as enum ('ADMIN', 'USER');
create type profit as enum ('EXPENSES', 'INCOME');

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

create table category
(
    id   serial primary key,
    name varchar not null
);

create table direction
(
    id          serial primary key,
    name        varchar not null,
    category_id integer references category (id)
);

create table season
(
    id          serial primary key,
    name        varchar not null,
    description text
);

create table breed
(
    id   serial primary key,
    name varchar not null
);

create table recommendation
(
    id          serial primary key,
    name        varchar not null,
    description text,
    season_id   integer references season (id),
    breed_id    integer references breed (id)
);

create table record
(
    id                serial primary key,
    user_id           integer references users (id),
    recommendation_id integer references recommendation (id)
);

create table user_pets
(
    id           serial primary key,
    user_id      integer references users (id),
    breed_id     integer references breed (id),
    direction_id integer references direction (id),
    quantity     integer not null
);

create table transactions
(
    id           serial primary key,
    profit       profit,
    price        integer,
    description  varchar,
    user_pets_id integer references user_pets (id)
);


alter table users
    add constraint fk_users_user_infos foreign key (user_info_id) references user_infos (id);

alter table direction
    add constraint fk_direction_category foreign key (category_id) references category (id);

alter table recommendation
    add constraint fk_recommendation_season foreign key (season_id) references season (id),
    add constraint fk_recommendation_breed foreign key (breed_id) references breed (id);

alter table record
    add constraint fk_record_users foreign key (user_id) references users (id),
    add constraint fk_record_recommendation foreign key (recommendation_id) references recommendation (id);

alter table user_pets
    add constraint fk_user_pets_users foreign key (user_id) references users (id),
    add constraint fk_user_pets_breed foreign key (breed_id) references breed (id),
    add constraint fk_user_direction foreign key (direction_id) references direction (id);

alter table transactions
    add constraint fk_transactions_user_pets foreign key (user_pets_id) references user_pets (id);