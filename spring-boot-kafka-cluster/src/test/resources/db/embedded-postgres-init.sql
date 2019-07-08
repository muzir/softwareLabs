-- noinspection SqlNoDataSourceInspectionForFile

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

DROP TABLE IF EXISTS product;
create table if not exists product
(
  id    bigint not null
    constraint product_pkey primary key,
  price NUMERIC(5, 2),
  name  varchar(255) UNIQUE
);
