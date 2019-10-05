DROP DATABASE IF EXISTS `logsdb`;
CREATE DATABASE `logsdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `logsdb`;

DROP TABLE IF EXISTS `request`;
DROP TABLE IF EXISTS `nginx_logfile_row`;
DROP TABLE IF EXISTS `nginx_logfile`;

CREATE TABLE `nginx_logfile` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `name` varchar(255) NOT NULL,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `request` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `request_method` varchar(255) DEFAULT NULL,
                           `request_uri` varchar(3000) DEFAULT NULL,
                           `request_uri_query_string` varchar(3000) DEFAULT NULL,
                           `server_protocol` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `request` ADD INDEX `uri_index` (`request_uri`);

CREATE TABLE `nginx_logfile_row` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `body_bytes_sent` bigint(20) DEFAULT NULL,
                                     `http_referer` varchar(255) DEFAULT NULL,
                                     `http_x_forwarded_for` varchar(3000) DEFAULT NULL,
                                     `remote_addr` varchar(255) DEFAULT NULL,
                                     `remote_user` varchar(255) DEFAULT NULL,
                                     `response_time` varchar(255) DEFAULT NULL,
                                     `status` varchar(255) DEFAULT NULL,
                                     `time_local` varchar(255) DEFAULT NULL,
                                     `unknown` varchar(255) DEFAULT NULL,
                                     `upstream_time` varchar(255) DEFAULT NULL,
                                     `user_agent` varchar(255) DEFAULT NULL,
                                     `owning_logfile_id` bigint(20) DEFAULT NULL,
                                     `request_id` bigint(20) DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `FK31r48bwkrfpr3pkeb96qg5da4` (`owning_logfile_id`),
                                     KEY `FKora5a244m5f0dryrxmhv2yhts` (`request_id`),
                                     CONSTRAINT `FK31r48bwkrfpr3pkeb96qg5da4` FOREIGN KEY (`owning_logfile_id`) REFERENCES `nginx_logfile` (`id`),
                                     CONSTRAINT `FKora5a244m5f0dryrxmhv2yhts` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;