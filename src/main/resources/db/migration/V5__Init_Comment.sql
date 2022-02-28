CREATE TABLE `comment` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `text` varchar(255) DEFAULT NULL,
                           `timestamp` datetime(6) DEFAULT NULL,
                           `message_id` int DEFAULT NULL,
                           PRIMARY KEY (`id`),
                            KEY `comment_fk` (`message_id`),
                           CONSTRAINT `comment_fk` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`)
);
