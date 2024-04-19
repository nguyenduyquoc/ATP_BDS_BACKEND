DROP DATABASE IF EXISTS bds_atp_db;
CREATE DATABASE bds_atp_db;
USE bds_atp_db;

CREATE TABLE province (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);

CREATE TABLE district (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          province_id INT NOT NULL,
                          FOREIGN KEY (province_id) REFERENCES province(id)
);


CREATE TABLE role (
                      id TINYINT AUTO_INCREMENT PRIMARY KEY,
                      role_name VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE account (
                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         name VARCHAR(100) NOT NULL,
                         mail VARCHAR(100) NOT NULL,
                         phone_number VARCHAR(10) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         is_deleted TINYINT DEFAULT 0,
                         role_id TINYINT,
                         FOREIGN KEY (role_id) REFERENCES role(id)
);


CREATE TABLE token (
                       id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                       token_name VARCHAR(255) NOT NULL,
                       expiry_date DATETIME NOT NULL,
                       account_id VARCHAR(36),
                       FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE type (
                      id TINYINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE project (
                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         thumbnail VARCHAR(255),
                         address VARCHAR(255),
                         status TINYINT DEFAULT 0,
                         start_date DATE,
                         end_date DATE,
                         qr_img VARCHAR(255) NOT NULL,
                         bank_number VARCHAR(100) NOT NULL,
                         bank_name VARCHAR(100) NOT NULL,
                         host_bank VARCHAR(100) NOT NULL,
                         investor VARCHAR(100) NOT NULL,
                         investor_phone VARCHAR(11) NOT NULL,
                         type_id TINYINT,
                         FOREIGN KEY (type_id) REFERENCES type(id),
                         district_id INT,
                         FOREIGN KEY (district_id) REFERENCES district(id),
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         is_deleted TINYINT DEFAULT 0
);

CREATE TABLE area (
                      id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                      name VARCHAR(100) NOT NULL,
                      project_id VARCHAR(36),
                      FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE land (
                      id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                      name VARCHAR(100) NOT NULL,
                      description TEXT,
                      thumbnail VARCHAR(255),
                      address VARCHAR(255),
                      status TINYINT DEFAULT 0,
                      price DECIMAL(18,2),
                      deposit DECIMAL(16,2),
                      acreage INT NOT NULL,
                      area_id VARCHAR(36),
                      FOREIGN KEY (area_id) REFERENCES area(id)
);

CREATE TABLE image (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       url VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       land_id VARCHAR(36),
                       FOREIGN KEY (land_id) REFERENCES land(id)
);

CREATE TABLE transaction (
                             id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                             status TINYINT DEFAULT 0,
                             user_id VARCHAR(36),
                             FOREIGN KEY (user_id) REFERENCES account(id),
                             land_id VARCHAR(36),
                             FOREIGN KEY (land_id) REFERENCES land(id),
                             stk VARCHAR(50),
                             bank_name VARCHAR(255),
                             otp VARCHAR(10),
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             is_deleted TINYINT DEFAULT 0

);
