#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    GRANT ALL PRIVILEGES ON DATABASE "$POSTGRES_DB" TO "$POSTGRES_USER";
    create table if not exists product
    (
      id  bigint not null constraint product_pkey primary key,
      price NUMERIC (5, 2),
      name  varchar(255) UNIQUE
    );
EOSQL
