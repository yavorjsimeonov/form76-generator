CREATE DATABASE  IF NOT EXISTS `form76-generator-db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `form76-generator-db`;

CREATE TABLE user (
    id VARCHAR(32) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(15) NOT NULL,
    active VARCHAR(255) NOT NULL,
    administration_id VARCHAR(255) NULL
);

-- Create administration table
CREATE TABLE administration (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    contact_person VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(255)
);

-- Create Locations table
CREATE TABLE location (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    administration_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (administration_id) REFERENCES location(id)
);

-- Create Devices table
CREATE TABLE device (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type ENUM('in', 'out') NOT NULL, -- Type can only be 'in' or 'out'
    location_id INT, -- FK to Locations
    FOREIGN KEY (location_id) REFERENCES location(id)
);


-- Create Reports table
CREATE TABLE report (
    id VARCHAR(32) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    location_id INT NOT NULL,
    creation_date DATETIME NOT NULL,
    cloud_storage_reference VARCHAR(255) NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location(id)
);