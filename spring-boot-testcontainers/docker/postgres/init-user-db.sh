#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE store;
    CREATE USER dbuser WITH PASSWORD 'password';
    GRANT ALL PRIVILEGES ON DATABASE store TO dbuser;
EOSQL
