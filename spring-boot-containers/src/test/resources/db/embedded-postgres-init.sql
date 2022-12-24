-- noinspection SqlNoDataSourceInspectionForFile

create table if not exists product
(
  id  bigint not null constraint product_pkey primary key,
  name  varchar(255) UNIQUE
);

create table if not exists orders
(
    id  varchar(255) not null constraint orders_pkey primary key,
    name  varchar(255) UNIQUE,
    order_status  varchar(30),
    create_time timestamp,
    update_time timestamp
);
