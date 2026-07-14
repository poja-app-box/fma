create extension if not exists "uuid-ossp";

create table if not exists "file"
(
    id varchar
        constraint file_pk primary key default uuid_generate_v4(),
    name varchar not null,
    uploader_email varchar not null,
    uploaded_at timestamp with time zone not null default now(),
    constraint unique_file_name_uploader_email unique (name, uploader_email)
);