-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table address(
    id bigserial not null primary key,
    street varchar(255)
);

create table client(
    id   bigserial not null primary key,
    name varchar(50) not null,
    client_address_id bigint,
    constraint client_address_fk foreign key (client_address_id) references address (id)
);

create table phones (
    id bigserial not null primary key,
    phone_number varchar(255) not null,
    client_id bigint,
    constraint unique_phone_number unique (phone_number),
    constraint client_id_fk foreign key (client_id) references client (id)
);




