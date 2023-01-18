-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema guides
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema guides
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `guides` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `guides` ;

-- -----------------------------------------------------
-- Table `guides`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`roles` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `role` VARCHAR(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `guides`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`users` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `age` INT NULL DEFAULT NULL,
                                                `email` VARCHAR(25) NULL DEFAULT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(25) NULL DEFAULT NULL,
    `address` VARCHAR(45) NULL DEFAULT NULL,
    `phone` VARCHAR(45) NULL DEFAULT NULL,
    `birthYear` YEAR NULL DEFAULT NULL,
    `gender` VARCHAR(45) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email` (`email` ASC) VISIBLE,
    UNIQUE INDEX `username` (`username` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `guides`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`users_roles` (
                                                      `role_id` INT NOT NULL,
                                                      `user_id` INT NOT NULL,
                                                      PRIMARY KEY (`role_id`, `user_id`),
    INDEX `FK_users_roles_user_id` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK_users_roles_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `guides`.`roles` (`id`),
    CONSTRAINT `FK_users_roles_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `guides`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `guides`.`guide`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`guide` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `gender` VARCHAR(45) NULL,
    `birthYear` YEAR NULL,
    `profile` VARCHAR(45) NULL,
    `image` VARCHAR(2048) NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `guides`.`trip`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`trip` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `name` VARCHAR(45) NULL,
    `date` DATE NULL,
    `time` TIME NULL,
    `location` VARCHAR(45) NULL,
    `duration` VARCHAR(45) NULL,
    `packingList` VARCHAR(255) NULL,
    `guide_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    INDEX `fk_trip_guide1_idx` (`guide_id` ASC) VISIBLE,
    CONSTRAINT `fk_trip_guide1`
    FOREIGN KEY (`guide_id`)
    REFERENCES `guides`.`guide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `guides`.`users_has_trip`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides`.`users_has_trip` (
                                                         `users_id` INT NOT NULL,
                                                         `trip_id` INT NOT NULL,
                                                         PRIMARY KEY (`users_id`, `trip_id`),
    INDEX `fk_users_has_trip_trip1_idx` (`trip_id` ASC) VISIBLE,
    INDEX `fk_users_has_trip_users1_idx` (`users_id` ASC) VISIBLE,
    CONSTRAINT `fk_users_has_trip_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `guides`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_users_has_trip_trip1`
    FOREIGN KEY (`trip_id`)
    REFERENCES `guides`.`trip` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
