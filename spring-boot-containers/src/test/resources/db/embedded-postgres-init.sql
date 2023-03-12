-- noinspection SqlNoDataSourceInspectionForFile

create table if not exists product
(
    id   bigint not null
        constraint product_pkey primary key,
    name varchar(255) UNIQUE
);

create table if not exists orders
(
    id           varchar(255) not null
        constraint orders_pkey primary key,
    name         varchar(255) UNIQUE,
    order_status varchar(30),
    version      integer      NOT NULL DEFAULT 0,
    create_time  timestamp,
    update_time  timestamp
);


create table if not exists queue_events
(
    id          varchar(255) not null
        constraint queue_events_pkey primary key,
    class_type  varchar(255),
    data        text,
    operation   varchar(255),
    retry_count int,
    event_state varchar(255),
    create_time timestamp,
    update_time timestamp
);

create table scheduled_tasks
(
    task_name            text                     not null,
    task_instance        text                     not null,
    task_data            bytea,
    execution_time       timestamp with time zone not null,
    picked               BOOLEAN                  not null,
    picked_by            text,
    last_success         timestamp with time zone,
    last_failure         timestamp with time zone,
    consecutive_failures INT,
    last_heartbeat       timestamp with time zone,
    version              BIGINT                   not null,
    PRIMARY KEY (task_name, task_instance)
);

CREATE INDEX execution_time_idx ON scheduled_tasks (execution_time);
CREATE INDEX last_heartbeat_idx ON scheduled_tasks (last_heartbeat);
