#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    GRANT ALL PRIVILEGES ON DATABASE "$POSTGRES_DB" TO "$POSTGRES_USER";
    create table if not exists product
    (
      id  bigint not null constraint product_pkey primary key,
      name  varchar(255) UNIQUE
    );
    create table if not exists orders
        (
          id  varchar(255) not null constraint orders_pkey primary key,
          name  varchar(255) UNIQUE,
          create_time timestamp,
          update_time timestamp
        );
EOSQL
