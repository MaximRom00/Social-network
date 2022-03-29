create table private_message
(
    id  int auto_increment primary key,
    text      varchar(1024) null,
    timestamp datetime(6)  null,
    receiver  int       null,
    sender    int       null
);
