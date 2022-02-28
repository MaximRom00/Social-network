insert into user (email, is_active, name, password, role, is_enabled)
    VALUE
    ('admin@admin.com', true, 'Max','$2a$06$8tOeV93mfGYXRAsDvkfc5eMEIdKelAXY3DCM.dp8apSwSjSxxDoDC', 'ADMIN', true),
    ('user@user.com', true, 'User','$2a$06$yl6YmzQZBkhJOw0.Zfsq7ek9T9Dru.wUI.9kwWqlfW.FD208D1lDy', 'USER', true),
    ('anton@anton.com', true, 'Anton','$2a$06$5SQMCyk8A0YqNfHvi29.5eBKjl8k/GkFbl9dhHyVyJEdD6JHU7r8G', 'USER', true),
    ('olga@olga.com', true, 'Olga','$2a$06$YB6cY41qxnUOiwL0xRIYQOJFCM5j94zl9hDINru.YL4apHbHQ5Fsq', 'USER', true);

insert into message(tag, text, user_id)
    VALUE
    ('Weather', 'Today is a good weather', 1),
    ('Weather', 'Its sunny outside', 1),
    ('Java', 'On the Server end, we recieve the data and reply back to the client. In Spring we can create a customized handler by using either TextWebSocketHandler or BinaryWebSocketHandler', 2),
    ('Java', 'BinaryWebSocketHandler is used to handle more enriched type of data like images. In our case since we need to handle only text so we will use TextWebSocketHandler.', 2),
    ('Sport', 'Manchester City vs Barcelona in 21:40', 4);
