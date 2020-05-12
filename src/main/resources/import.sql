DROP SCHEMA IF EXISTS `loyalty` ;

CREATE SCHEMA `loyalty` ;

CREATE TABLE `loyalty`.`loyalty_account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `balance` INT NULL,
  `tier` INT NULL,
  `enabled` TINYINT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `loyalty`.`loyalty_transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NULL,
  `points` INT NULL,
  `name` VARCHAR(45) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`));

INSERT INTO `loyalty`.`loyalty_account` (`id`, `balance`, `tier`, `enabled`) VALUES ('1', '333', '1', '1');
INSERT INTO `loyalty`.`loyalty_account` (`id`, `balance`, `tier`, `enabled`) VALUES ('2', '122', '1', '1');
INSERT INTO `loyalty`.`loyalty_account` (`id`, `balance`, `tier`, `enabled`) VALUES ('3', '100', '2', '1');

INSERT INTO `loyalty`.`loyalty_transaction` (`id`, `account_id`, `points`, `name`, `date`) VALUES ('1', '1', '100', 'KFC', '2019-01-19 14:55:02');
INSERT INTO `loyalty`.`loyalty_transaction` (`id`, `account_id`, `points`, `name`, `date`) VALUES ('2', '1', '80', 'Carrefour', '2020-01-19 14:55:02');
INSERT INTO `loyalty`.`loyalty_transaction` (`id`, `account_id`, `points`, `name`, `date`) VALUES ('3', '1', '75', 'Car Wash', '2020-02-19 14:55:02');
INSERT INTO `loyalty`.`loyalty_transaction` (`id`, `account_id`, `points`, `name`, `date`) VALUES ('4', '2', '110', 'PizzaHut', '2020-01-19 14:55:02');



