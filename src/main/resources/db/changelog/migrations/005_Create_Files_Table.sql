create table files
(
    id           bigint generated by default as identity
        constraint files_pkey
            primary key,
    deleteddate timestamp,
    filename     varchar(255),
    deleted   boolean not null,
    loaddate    timestamp,
    size         bigint  not null,
    username     varchar(255)
);