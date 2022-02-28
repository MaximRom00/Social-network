CREATE TABLE `subscriptions` (
                                 `user_id` int NOT NULL,
                                 `subscriber_id` int NOT NULL,
                                 PRIMARY KEY (`user_id`,`subscriber_id`),
                                 KEY `subscriber_id_fk` (`subscriber_id`),
                                 CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                 CONSTRAINT `subscriber_id_fk` FOREIGN KEY (`subscriber_id`) REFERENCES `user` (`id`)
);