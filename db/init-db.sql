CREATE DATABASE  IF NOT EXISTS `from76-generator-db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `from76-generator-db`;


CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- Create Locations table
CREATE TABLE location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Devices table
CREATE TABLE device (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('in', 'out') NOT NULL, -- Type can only be 'in' or 'out'
    name VARCHAR(255) NOT NULL,
    location_id INT, -- FK to Locations
    FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE
);

-- Create Reports table
CREATE TABLE report (
    id INT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    creation_date DATETIME NOT NULL,
    cloud_storage_reference VARCHAR(255) NOT NULL
);

-- Create administration table
CREATE TABLE administration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT, -- FK to Users
    location_id INT, -- FK to Locations
    report_id INT, -- FK to Reports
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE
);
