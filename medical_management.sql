-- 1. Create the database
CREATE DATABASE medicalshop;

-- 2. Use the database
USE medicalshop;

-- 3. Create table for application users (used by LoginPage)
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  fullname VARCHAR(100),
  role ENUM('admin','user') DEFAULT 'user'
);

-- 4. Create table for medicines (used by Dashboard)
CREATE TABLE medicines (
  med_id INT AUTO_INCREMENT PRIMARY KEY,
  med_name VARCHAR(150) NOT NULL,
  supplier VARCHAR(100),
  quantity INT NOT NULL DEFAULT 0,
  cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  mfg_date DATE,
  exp_date DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Example initial data: admin user and a sample medicine
INSERT INTO users (username, password, fullname, role)
VALUES ('admin', 'admin123', 'Administrator', 'admin');

INSERT INTO medicines (med_name, supplier, quantity, cost, mfg_date, exp_date)
VALUES ('Paracetamol 500mg', 'ABC Pharma', 100, 1.50, '2024-07-01', '2026-06-30');



