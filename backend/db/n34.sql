CREATE TABLE `user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `username`     varchar(64) NOT NULL,
    `password`     char(60)    NOT NULL,
    `email`        varchar(64) NOT NULL,
    `nickname`     varchar(64) NOT NULL,
    `time_created` datetime DEFAULT NULL,
    `time_updated` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8