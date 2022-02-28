
delete from message;

ALTER TABLE message AUTO_INCREMENT=1;

insert into message (tag, text, user_id, file_name) VALUES

('hello', 'first message', 1, null),
('day of week', 'monday', 1, null),
('hello', 'How are you?', 2, null),
('day of week', 'Today is monday!', 2, null),
('sport', 'barcelona vs real madrid', 3, null);


