
create table address(
    id bigserial not null primary key,
    street varchar(255)

);

create table client(
    id bigserial not null primary key,
    name varchar(50) not null,
    client_address_id bigint,
    constraint client_address_fk foreign key (client_address_id) references address (id)
);

create table phones (
    id bigserial not null primary key,
    phone_number varchar(255) not null,
    client_id bigint references client (id)
);




