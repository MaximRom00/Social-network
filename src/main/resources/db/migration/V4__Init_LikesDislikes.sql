create table message_like(
    user_id    int not null,
    message_id int not null,
    primary key (user_id, message_id),
    constraint user_like_fk
        foreign key (user_id) references user (id),
    constraint message_like_fk
        foreign key (message_id) references message (id)
);
create table message_dislike
(
    user_id    int not null,
    message_id int not null,
    primary key (user_id, message_id),
    constraint user_dislike_fk
        foreign key (message_id) references message (id),
    constraint message_dislike_fk
        foreign key (user_id) references user (id)
);




