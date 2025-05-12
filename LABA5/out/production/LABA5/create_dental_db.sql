-- Создание базы данных стоматологической клиники
CREATE DATABASE dental_clinic;

-- Подключение к созданной базе данных
\c dental_clinic;

-- Создание таблицы пациентов
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(100) NOT NULL,
    is_being_treated BOOLEAN DEFAULT FALSE
);

-- Создание таблицы стоматологов
CREATE TABLE dentists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    specialization VARCHAR(100) NOT NULL
);

-- Создание таблицы ассистентов
CREATE TABLE assistants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    qualification VARCHAR(100) NOT NULL
);

-- Создание таблицы визитов
CREATE TABLE visits (
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    dentist_id INT,
    assistant_id INT,
    visit_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    treatment_done BOOLEAN DEFAULT FALSE,
    notes TEXT,
    CONSTRAINT fk_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_dentist
        FOREIGN KEY (dentist_id)
        REFERENCES dentists (id)
        ON DELETE SET NULL,
    CONSTRAINT fk_assistant
        FOREIGN KEY (assistant_id)
        REFERENCES assistants (id)
        ON DELETE SET NULL,
    -- Должен быть указан либо стоматолог, либо ассистент (либо оба)
    CONSTRAINT chk_staff_present 
        CHECK (dentist_id IS NOT NULL OR assistant_id IS NOT NULL)
);

-- Создание индексов для ускорения поиска
CREATE INDEX idx_patients_disease ON patients (disease);
CREATE INDEX idx_dentists_specialization ON dentists (specialization);
CREATE INDEX idx_visits_patient ON visits (patient_id);
CREATE INDEX idx_visits_dentist ON visits (dentist_id);
CREATE INDEX idx_visits_date ON visits (visit_date);

-- Создание представления для получения информации о необработанных пациентах
CREATE VIEW untreated_patients AS
SELECT p.*
FROM patients p
WHERE p.is_being_treated = FALSE;

-- Создание представления для получения полной информации о визитах
CREATE VIEW visit_details AS
SELECT 
    v.id AS visit_id,
    v.visit_date,
    v.treatment_done,
    v.notes,
    p.id AS patient_id,
    p.name AS patient_name,
    p.disease,
    d.id AS dentist_id,
    d.name AS dentist_name,
    d.specialization,
    a.id AS assistant_id,
    a.name AS assistant_name,
    a.qualification
FROM visits v
JOIN patients p ON v.patient_id = p.id
LEFT JOIN dentists d ON v.dentist_id = d.id
LEFT JOIN assistants a ON v.assistant_id = a.id;

-- Пример добавления начальных данных
INSERT INTO patients (name, age, disease) VALUES
    ('Иван Петров', 35, 'Кариес'),
    ('Анна Сидорова', 42, 'Пульпит'),
    ('Виктор Смирнов', 28, 'Пародонтит');

INSERT INTO dentists (name, age, specialization) VALUES
    ('Елена Иванова', 45, 'Терапевт'),
    ('Сергей Козлов', 53, 'Хирург'),
    ('Мария Соколова', 38, 'Ортодонт');

INSERT INTO assistants (name, age, qualification) VALUES
    ('Ольга Белова', 29, 'Младший ассистент'),
    ('Дмитрий Волков', 34, 'Старший ассистент'); 