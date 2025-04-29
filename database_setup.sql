-- Create database
CREATE DATABASE IF NOT EXISTS medical_db;
USE medical_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- Insert default admin user
INSERT INTO users (username, password) VALUES ('admin', 'password')
ON DUPLICATE KEY UPDATE password = 'password';

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100)
);

-- Medicines table
CREATE TABLE IF NOT EXISTS medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    drug_class VARCHAR(100),
    indication VARCHAR(200),
    indication_description TEXT,
    dosage TEXT,
    admin_description TEXT,
    contraindication TEXT,
    side_effect TEXT,
    pregnancy_lactation TEXT,
    precautious TEXT,
    treatment_duration TEXT,
    storage_condition TEXT,
    link VARCHAR(255)
);

-- Transcriptions table
CREATE TABLE IF NOT EXISTS transcriptions (
    trans_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id VARCHAR(36) NOT NULL,
    description TEXT,
    medical_specialty VARCHAR(100),
    sample_name VARCHAR(100),
    transcription TEXT,
    keywords TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Prescriptions table
CREATE TABLE IF NOT EXISTS prescriptions (
    pres_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(50),
    medicine_name VARCHAR(100),
    dosage TEXT,
    storage_cond TEXT,
    medicine_id INT,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

-- Insert sample data
INSERT INTO patients (id, first_name, last_name, phone, email)
VALUES 
    (UUID(), 'John', 'Doe', '555-1234', 'john@example.com'),
    (UUID(), 'Jane', 'Smith', '555-5678', 'jane@example.com')
ON DUPLICATE KEY UPDATE first_name = VALUES(first_name);

INSERT INTO medicines (name, drug_class, indication, indication_description, 
                      dosage, admin_description, contraindication, side_effect, 
                      pregnancy_lactation, precautious, treatment_duration, 
                      storage_condition, link)
VALUES 
    ('Aspirin', 'Analgesic', 'Pain relief', 'Used for mild to moderate pain relief', 
     '1-2 tablets every 4-6 hours', 'Take with food or water', 'Allergy to salicylates', 
     'Stomach upset, bleeding', 'Not recommended during pregnancy', 'Use with caution in elderly', 
     'As needed for pain', 'Store at room temperature', 'https://www.example.com/aspirin'),
    
    ('Amoxicillin', 'Antibiotic', 'Bacterial infections', 'Used for various bacterial infections', 
     '500mg three times daily', 'Take with or without food', 'Allergy to penicillins', 
     'Diarrhea, nausea', 'Generally safe during pregnancy', 'Complete full course of treatment', 
     '7-10 days typically', 'Store at room temperature', 'https://www.example.com/amoxicillin')
ON DUPLICATE KEY UPDATE name = VALUES(name);