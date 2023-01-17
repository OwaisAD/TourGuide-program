-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema boats
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema boats
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `boats` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `boats` ;

-- -----------------------------------------------------
-- Table `boats`.`harbour`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`harbour` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `name` VARCHAR(45) NOT NULL,
    `address` VARCHAR(45) NOT NULL,
    `capacity` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`boat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`boat` (
                                              `id` INT NOT NULL AUTO_INCREMENT,
                                              `brand` VARCHAR(45) NOT NULL,
    `make` VARCHAR(45) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `image` VARCHAR(2048) NOT NULL,
    `harbour_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    INDEX `fk_boat_harbour1_idx` (`harbour_id` ASC) VISIBLE,
    CONSTRAINT `fk_boat_harbour1`
    FOREIGN KEY (`harbour_id`)
    REFERENCES `boats`.`harbour` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`owner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`owner` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `name` VARCHAR(45) NOT NULL,
    `address` VARCHAR(200) NOT NULL,
    `phone` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`owner_has_boat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`owner_has_boat` (
                                                        `owner_id` INT NOT NULL,
                                                        `boat_id` INT NOT NULL,
                                                        PRIMARY KEY (`owner_id`, `boat_id`),
    INDEX `fk_owner_has_boat_boat1_idx` (`boat_id` ASC) VISIBLE,
    INDEX `fk_owner_has_boat_owner1_idx` (`owner_id` ASC) VISIBLE,
    CONSTRAINT `fk_owner_has_boat_boat1`
    FOREIGN KEY (`boat_id`)
    REFERENCES `boats`.`boat` (`id`),
    CONSTRAINT `fk_owner_has_boat_owner1`
    FOREIGN KEY (`owner_id`)
    REFERENCES `boats`.`owner` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`roles` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `role` VARCHAR(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`users` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `age` INT NULL DEFAULT NULL,
                                               `password` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(25) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username` (`username` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `boats`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boats`.`users_roles` (
                                                     `role_id` INT NOT NULL,
                                                     `user_id` INT NOT NULL,
                                                     PRIMARY KEY (`role_id`, `user_id`),
    INDEX `FK_users_roles_user_id` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK_users_roles_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `boats`.`roles` (`id`),
    CONSTRAINT `FK_users_roles_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `boats`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
