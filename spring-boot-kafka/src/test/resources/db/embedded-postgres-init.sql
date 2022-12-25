-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS product;
create table if not exists product
(
    id    bigint not null
        constraint product_pkey primary key,
    name  varchar(255) UNIQUE,
    price NUMERIC(5, 2)
);
