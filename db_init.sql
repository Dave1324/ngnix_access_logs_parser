
DROP TABLE IF EXISTS `request`;
DROP TABLE IF EXISTS `nginx_logfile_row`;
DROP TABLE IF EXISTS `nginx_logfile`;

CREATE TABLE `nginx_logfile` (
                         `name` varchar(255) NOT NULL,
                         `created_at` datetime(6) DEFAULT NULL,
                         `version` bigint(20) DEFAULT NULL,
                         PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `request` (
                           `id` bigint(20) NOT NULL,
                           `request_method` varchar(255) DEFAULT NULL,
                           `request_uri` varchar(3000) DEFAULT NULL,
                           `server_protocol` varchar(255) DEFAULT NULL,
                           `version` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `nginx_logfile_row` (
  `id` bigint(20) NOT NULL,
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
  `version` bigint(20) DEFAULT NULL,
  `owning_logfile_name` varchar(255) DEFAULT NULL,
  `request_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK31r48bwkrfpr3pkeb96qg5da4` (`owning_logfile_name`),
  KEY `FKora5a244m5f0dryrxmhv2yhts` (`request_id`),
  CONSTRAINT `FK31r48bwkrfpr3pkeb96qg5da4` FOREIGN KEY (`owning_logfile_name`) REFERENCES `nginx_logfile` (`name`),
  CONSTRAINT `FKora5a244m5f0dryrxmhv2yhts` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;