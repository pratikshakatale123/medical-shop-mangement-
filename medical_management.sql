-- -----------------------------------------------------
-- DATABASE CREATION
-- -----------------------------------------------------
CREATE DATABASE medical_management;
USE medical_management;

-- -----------------------------------------------------
-- TABLE: users
-- -----------------------------------------------------
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- -----------------------------------------------------
-- TABLE: manager
-- -----------------------------------------------------
CREATE TABLE manager (
    auth_number INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- TABLE: customers
-- -----------------------------------------------------
CREATE TABLE customers (
    cust_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    sex VARCHAR(10),
    problem VARCHAR(200)
);

-- -----------------------------------------------------
-- TABLE: medicines
-- -----------------------------------------------------
CREATE TABLE medicines (
    med_id INT AUTO_INCREMENT PRIMARY KEY,
    med_name VARCHAR(100) NOT NULL,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    quantity INT NOT NULL,
    mrp DECIMAL(10,2),
    exp_date DATE
);

-- -----------------------------------------------------
-- TABLE: report
-- -----------------------------------------------------
CREATE TABLE report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    sales_report TEXT,
    customer_report TEXT,
    medicine_stock TEXT
);

-- -----------------------------------------------------
-- INSERT SAMPLE DATA (for testing)
-- -----------------------------------------------------

-- Admin / user login
INSERT INTO users (username, password, name, email)
VALUES ('admin', 'admin123', 'System Admin', 'admin@medical.com');

-- Manager data linked to user
INSERT INTO manager (username)
VALUES ('admin');

-- Some sample customers
INSERT INTO customers (name, age, sex, problem)
VALUES 
('Rahul Patil', 32, 'Male', 'Fever'),
('Sneha Kulkarni', 27, 'Female', 'Headache');

-- Some sample medicines
INSERT INTO medicines (med_name, product_code, quantity, mrp, exp_date)
VALUES 
('Paracetamol', 'MED001', 50, 25.00, '2026-12-31'),
('Cough Syrup', 'MED002', 30, 60.00, '2025-08-15'),
('Antibiotic Capsule', 'MED003', 80, 15.50, '2027-02-10');

-- Sample report data
INSERT INTO report (sales_report, customer_report, medicine_stock)
VALUES 
('Weekly sales increased by 12%', '5 new customers added this week', 'Paracetamol stock low, reorder needed');

-- -----------------------------------------------------
-- CHECK DATA
-- -----------------------------------------------------
SELECT * FROM users;
SELECT * FROM manager;
SELECT * FROM customers;
SELECT * FROM report;
SELECT * FROM medicines;
