use tim;
drop
database if exists tim;
CREATE
DATABASE `tim` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;


drop TABLE if exists `tim_group_message`;
CREATE TABLE `tim_group_message`
(
    `id`         bigint                           NOT NULL AUTO_INCREMENT,
    `msg_id`     bigint                           NOT NULL,
    `user_id`    bigint                           NOT NULL,
    `group_id`   bigint                           NOT NULL DEFAULT '0',
    `msg`        varchar(256) COLLATE utf8mb4_bin NOT NULL,
    `createTime` timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updateTime` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `msg_id_UNIQUE` (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop TABLE if exists `tim_single_message`;
CREATE TABLE `tim_single_message`
(
    `id`         bigint                           NOT NULL AUTO_INCREMENT,
    `msg_id`     bigint                           NOT NULL,
    `user_id`    bigint                           NOT NULL,
    `to_user_id` bigint                           NOT NULL,
    `msg`        varchar(256) COLLATE utf8mb4_bin NOT NULL,
    `createTime` timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updateTime` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `msg_id_UNIQUE` (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

drop TABLE if exists `tim_user`;
CREATE TABLE `tim_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `user_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
                            `gender` varchar(2) COLLATE utf8mb4_bin DEFAULT ' ',
                            `user_nick` varchar(64) COLLATE utf8mb4_bin DEFAULT ' ',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin