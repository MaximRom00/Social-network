CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `email` varchar(255) not null ,
                        `is_active` bit(1) NOT NULL,
                        `name` varchar(255) not null ,
                        `password` varchar(255) not null ,
                        `role` varchar(255) not null ,
                        `is_enabled` bit(1) NOT NULL,
                        PRIMARY KEY (`id`)
);
CREATE TABLE `message` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `tag` varchar(255) DEFAULT NULL,
                           `text` varchar(2048) not null ,
                           `user_id` int DEFAULT NULL,
                           `file_name` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `message_user_fk` (`user_id`),
                           CONSTRAINT `message_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
CREATE TABLE `secure_token` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `time_stamp` DATETIME NOT NULL,
                                `token` varchar(255) NOT NULL,
                                `user_id` int NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `secure_user_fk` (`user_id`),
                                CONSTRAINT `secure_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
