-- noinspection SqlNoDataSourceInspectionForFile

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

create table if not exists product
(
  id  bigint not null constraint product_pkey primary key,
  name  varchar(255) UNIQUE
);
