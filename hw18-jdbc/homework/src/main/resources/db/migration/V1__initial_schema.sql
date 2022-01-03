create table clients
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table managers
(
    no bigserial not null primary key,
    label varchar(50),
    param1 varchar(50)
);
