
CREATE USER dbuser WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE store TO dbuser;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO dbuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO dbuser;

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

create table if not exists product
(
  id  bigint not null constraint product_pkey primary key,
  name  varchar(255) UNIQUE
);


GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dbuser
