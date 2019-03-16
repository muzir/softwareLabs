CREATE USER dbuser WITH PASSWORD 'password';


CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

-- Set permission to database user for sequences.
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO dbuser;

-- Set permission to database user for schema.
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO dbuser;



create table if not exists product
(
  id  bigint not null constraint product_pkey primary key,
  name  varchar(255) UNIQUE
);

-- Set permission to database user for tables.
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dbuser;
