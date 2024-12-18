DROP DATABASE IF EXISTS opendata;
CREATE DATABASE opendata;
USE opendata;

CREATE TABLE Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL
);

CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES Role(id)
);

INSERT INTO `opendata`.`role` (`type`) VALUES ('ADMIN');
INSERT INTO `opendata`.`role` (`type`) VALUES ('USER');

INSERT INTO `user` (`id`,`first_name`,`last_name`,`email`,`password`,`created_at`,`role_id`) VALUES (4,'admin','admin','admin@gmail.com','$2a$10$0XI0R5bD9meVsECTe3sbtOi1l7epov1c/sYLABv0NlCnuRthCP9lK','2024-10-31 07:18:59',1);

/* Creation de la table sujet */
CREATE TABLE Theme(
   id INT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(255) NOT NULL
);

INSERT INTO `theme` (`id`,`name`) VALUES (1,'Démographie et Analyse du territoire');
INSERT INTO `theme` (`id`,`name`) VALUES (2,'Déplacements et Mobilités');

CREATE TABLE Wording(
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     download INT,
     license VARCHAR(255) NOT NUll,
     modifie DATETIME,
     pdf BLOB,
     csv BLOB,
     xml BLOB,
     territory VARCHAR(255),
     descriptions LONGTEXT NOT NULL,
     langue VARCHAR(255) NOT NULL,
     theme_id INT,  -- Utilise un ID pour référencer l'objet Theme
     FOREIGN KEY (theme_id) REFERENCES Theme(id)

);