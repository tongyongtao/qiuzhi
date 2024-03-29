  DROP TABLE IF EXISTS `question`;
  CREATE TABLE `question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NULL,
  `label` VARCHAR(255) NOT NULL,
  `reward` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `date_index` (`created_date` ASC),
  INDEX `label_index` (`label` ASC)
  )ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `user`;
  CREATE TABLE `user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `email` varchar(32) NOT NULL DEFAULT '',
    `password` varchar(128) NOT NULL DEFAULT '',
    `nick_name` varchar(64) NOT NULL DEFAULT '',
    `salt` varchar(32) NOT NULL DEFAULT '',
    `city` varchar(64),
    `sex` int(2),
    `sign` TEXT,
    `created_date` DATETIME NOT NULL,
    `head_url` varchar(256) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `oauth`;
  CREATE TABLE `oauth` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `open_id` VARCHAR(255) NOT NULL,
  `app_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `oauth_index` (`app_type` ASC, `open_id` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `login_ticket`;
  CREATE TABLE `login_ticket` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `ticket` VARCHAR(45) NOT NULL,
    `expired` DATETIME NOT NULL,
    `status` INT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `comment`;
  CREATE TABLE `comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NOT NULL,
  `user_id` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `like_count` INT NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


  DROP TABLE IF EXISTS `message`;
  CREATE TABLE `message` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `from_id` INT NULL,
    `to_id` INT NULL,
    `content` TEXT NULL,
    `created_date` DATETIME NULL,
    `has_read` INT NULL,
    `conversation_id` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `conversation_index` (`conversation_id` ASC),
    INDEX `created_date` (`created_date` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

  DROP TABLE IF EXISTS `collect`;
  CREATE TABLE `collect` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `feed`;
  CREATE TABLE `feed` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `created_date` DATETIME NULL,
    `user_id` INT NULL,
    `data` TEXT NULL,
    `type` INT NULL,
    PRIMARY KEY (`id`),
    INDEX `user_index` (`user_id` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;