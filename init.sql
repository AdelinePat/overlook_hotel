-- /!\ First attempt creating database, not tested yet !!!

CREATE DATABASE IF NOT EXISTS overlook_hotel
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE overlook_hotel;
SET NAMES 'utf8mb4';

CREATE TABLE IF NOT EXISTS manager (
        id_manager INT AUTO_INCREMENT PRIMARY KEY,
        lastname VARCHAR(100) NOT NULL,
        firstname VARCHAR(100) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        salt VARCHAR(255),
        password VARCHAR(255)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- client
CREATE TABLE IF NOT EXISTS client (
        id_client INT AUTO_INCREMENT PRIMARY KEY,
        lastname VARCHAR(100) NOT NULL,
        firstname VARCHAR(100) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        phone VARCHAR(15) NOT NULL,
        salt VARCHAR(255),
        password VARCHAR(255)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS fidelity (
        id_fidelity INT AUTO_INCREMENT PRIMARY KEY,
        id_client INT NOT NULL,
        value INT NOT NULL,
        FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE CASCADE
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- employee
CREATE TABLE IF NOT EXISTS employee (
        id_employee INT AUTO_INCREMENT PRIMARY KEY,
        lastname VARCHAR(100) NOT NULL,
        firstname VARCHAR(100) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        job ENUM('RECEPTIONNISTE',
                 'AGENT_ENTRETIEN',
                 'HOTE',
                 'COORDINATEUR',
                 'BAGAGISTE',
                 'CHEF_CUISINIER') NOT NULL,
        salt VARCHAR(255),
        password VARCHAR(255)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS formation (
         id_formation INT AUTO_INCREMENT PRIMARY KEY,
         start DATE NOT NULL,
         end DATE NOT NULL,
         title VARCHAR(50)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS schedule (
        id_schedule INT AUTO_INCREMENT PRIMARY KEY,
        day_of_week ENUM('LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI', 'DIMANCHE') NOT NULL,
        shift ENUM('MATIN', 'APRES_MIDI', 'SOIR', 'NUIT') NOT NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS vacation (
        id_vacation INT AUTO_INCREMENT PRIMARY KEY,
        id_employee INT NOT NULL,
        start DATE NOT NULL,
        end DATE NOT NULL,
        FOREIGN KEY (id_employee) REFERENCES employee(id_employee) ON DELETE CASCADE
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS employee_schedule (
        id_employee_schedule INT AUTO_INCREMENT PRIMARY KEY,
        id_employee INT NOT NULL,
        id_schedule INT,
        FOREIGN KEY (id_employee) REFERENCES employee(id_employee) ON DELETE CASCADE,
        FOREIGN KEY (id_schedule) REFERENCES schedule(id_schedule) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS employee_formation (
        id_employee_formation INT AUTO_INCREMENT PRIMARY KEY,
        id_employee INT NOT NULL,
        id_formation INT,
        FOREIGN KEY (id_employee) REFERENCES employee(id_employee) ON DELETE CASCADE,
        FOREIGN KEY (id_formation) REFERENCES formation(id_formation) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- room
CREATE TABLE IF NOT EXISTS room (
        id_room INT AUTO_INCREMENT PRIMARY KEY,
        number INT NOT NULL,
        capacity INT NOT NULL,
        description VARCHAR(500),
        standing ENUM('SUPERIEURE', 'DE_LUXE', 'SUPERBE_VUE', 'SIMPLE') NOT NULL,
        type ENUM('SIMPLE', 'DOUBLE'),
        night_price DECIMAL(6,2) NOT NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS room_bonus (
        id_room_bonus INT AUTO_INCREMENT PRIMARY KEY,
        type ENUM('TV', 'SECHOIR', 'MINI_BAR', 'BOUILLOIRE', 'FAUTEUIL_MASSANT', 'PC_GAMING', 'JACUZZI') NOT NULL,
        daily_price DECIMAL(6,2) NOT NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS room_reservation (
        id_room_reservation INT AUTO_INCREMENT PRIMARY KEY,
        id_client INT NULL,
        creation DATE NOT NULL,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        status BOOLEAN NOT NULL,
        payment_date DATETIME,
        total_price DECIMAL(6,2) NULL,
        FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS room_link_reservation (
        id_room_link_reservation INT AUTO_INCREMENT PRIMARY KEY,
        id_room_reservation INT NOT NULL,
        id_room INT,
        FOREIGN KEY (id_room_reservation) REFERENCES room_reservation(id_room_reservation) ON DELETE CASCADE,
        FOREIGN KEY (id_room) REFERENCES room(id_room) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS room_link_bonus (
        id_room_link_bonus INT AUTO_INCREMENT PRIMARY KEY,
        id_room_bonus INT,
        id_room INT,
        FOREIGN KEY (id_room_bonus) REFERENCES room_bonus(id_room_bonus) ON DELETE SET NULL,
        FOREIGN KEY (id_room) REFERENCES room(id_room) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- event
CREATE TABLE IF NOT EXISTS place (
        id_place INT AUTO_INCREMENT PRIMARY KEY,
        type ENUM('SALLE_DE_REUNION', 'PISCINE', 'SPA', 'TENNIS', 'PLACARD_A_BALAIS', 'SALLE_SUR_DEMANDE') NOT NULL,
        capacity INT NOT NULL,
        hourly_price DECIMAL(6,2) NOT NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS event_reservation (
        id_event_reservation INT AUTO_INCREMENT PRIMARY KEY,
        id_client INT,
        event ENUM('MARIAGE', 'FETE', 'ANNIVERSAIRE', 'ENTERREMENT', 'REUNION', 'AUTRE') NOT NULL,
        start_date DATETIME NOT NULL,
        end_date DATETIME NOT NULL,
        total_price DECIMAL(6,2) NOT NULL,
        FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS event_link_place (
        id_event_link_place INT AUTO_INCREMENT PRIMARY KEY,
        id_event_reservation INT NOT NULL,
        id_place INT,
        FOREIGN KEY (id_event_reservation) REFERENCES event_reservation(id_event_reservation) ON DELETE CASCADE,
        FOREIGN KEY (id_place) REFERENCES place(id_place) ON DELETE SET NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- depend on multiple tables
CREATE TABLE IF NOT EXISTS feedback (
        id_feedback INT AUTO_INCREMENT PRIMARY KEY,
        id_room_reservation INT,
        rate INT NOT NULL,
        comment VARCHAR(500),
        FOREIGN KEY (id_room_reservation) REFERENCES room_reservation(id_room_reservation) ON DELETE CASCADE
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS response (
        id_response INT AUTO_INCREMENT PRIMARY KEY,
        id_manager INT,
        id_feedback INT NOT NULL,
        answer VARCHAR(500),
        FOREIGN KEY (id_manager) REFERENCES manager(id_manager) ON DELETE SET NULL,
        FOREIGN KEY (id_feedback) REFERENCES feedback(id_feedback) ON DELETE CASCADE
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- fake data
INSERT INTO manager (lastname, firstname, email, salt, password)
VALUES
        ('Dupont', 'Jean', 'jean.dupont@example.com', 'somesalt1', 'password1'),
        ('Martin', 'Sophie', 'sophie.martin@example.com', 'somesalt2', 'password2');

INSERT INTO client (lastname, firstname, email, phone, salt, password)
VALUES
        ('Durand', 'Alice', 'alice.durand@example.com', '0123456789', 'salt1', 'pass1'),
        ('Moreau', 'Bob', 'bob.moreau@example.com', '0987654321', 'salt2', 'pass2'),
        ('Petit', 'Claire', 'claire.petit@example.com', '0612345678', 'salt3', 'pass3'),
        ('Roux', 'David', 'david.roux@example.com', '0654321987', 'salt4', 'pass4'),
        ('Blanc', 'Emma', 'emma.blanc@example.com', '0623456789', 'salt5', 'pass5'),
        ('Lemoine', 'Franck', 'franck.lemoine@example.com', '0678912345', 'salt6', 'pass6'),
        ('Gautier', 'Hélène', 'helene.gautier@example.com', '0612349876', 'salt7', 'pass7'),
        ('Faure', 'Isabelle', 'isabelle.faure@example.com', '0656781234', 'salt8', 'pass8'),
        ('Chevalier', 'Julien', 'julien.chevalier@example.com', '0621987654', 'salt9', 'pass9'),
        ('Michel', 'Karine', 'karine.michel@example.com', '0612345671', 'salt10', 'pass10'),
        ('Perrin', 'Laurent', 'laurent.perrin@example.com', '0623456782', 'salt11', 'pass11'),
        ('Henry', 'Marie', 'marie.henry@example.com', '0678123490', 'salt12', 'pass12'),
        ('Noel', 'Nicolas', 'nicolas.noel@example.com', '0612987654', 'salt13', 'pass13'),
        ('Robin', 'Olivia', 'olivia.robin@example.com', '0654321765', 'salt14', 'pass14'),
        ('Giraud', 'Paul', 'paul.giraud@example.com', '0612349872', 'salt15', 'pass15'),
        ('Colin', 'Quentin', 'quentin.colin@example.com', '0623451987', 'salt16', 'pass16'),
        ('Marchand', 'Roxane', 'roxane.marchand@example.com', '0678123459', 'salt17', 'pass17'),
        ('Lucas', 'Sylvain', 'sylvain.lucas@example.com', '0612348765', 'salt18', 'pass18'),
        ('Bertrand', 'Thomas', 'thomas.bertrand@example.com', '0623456780', 'salt19', 'pass19'),
        ('Lefevre', 'Valérie', 'valerie.lefevre@example.com', '0678123491', 'salt20', 'pass20');

INSERT INTO employee (lastname, firstname, email, job, salt, password)
VALUES
        ('Legrand', 'Pierre', 'pierre.legrand@example.com', 'RECEPTIONNISTE', 'saltE1', 'passE1'),
        ('Bernard', 'Marie', 'marie.bernard@example.com', 'AGENT_ENTRETIEN', 'saltE2', 'passE2'),
        ('Faure', 'Luc', 'luc.faure@example.com', 'HOTE', 'saltE3', 'passE3'),
        ('Dupuis', 'Anne', 'anne.dupuis@example.com', 'COORDINATEUR', 'saltE4', 'passE4'),
        ('Meyer', 'Sébastien', 'sebastien.meyer@example.com', 'BAGAGISTE', 'saltE5', 'passE5'),
        ('Renaud', 'Céline', 'celine.renaud@example.com', 'CHEF_CUISINIER', 'saltE6', 'passE6'),
        ('Benoit', 'Éric', 'eric.benoit@example.com', 'RECEPTIONNISTE', 'saltE7', 'passE7'),
        ('Girard', 'Fanny', 'fanny.girard@example.com', 'AGENT_ENTRETIEN', 'saltE8', 'passE8'),
        ('Robert', 'Hugo', 'hugo.robert@example.com', 'HOTE', 'saltE9', 'passE9'),
        ('Meyera', 'Sébastiena', 'sebastiena.meyera@example.com', 'BAGAGISTE', 'saltE5', 'passE5'),
        ('Vidal', 'Inès', 'ines.vidal@example.com', 'COORDINATEUR', 'saltE10', 'passE10');


INSERT INTO room (number, capacity, description, standing, type, night_price)
VALUES
        (101, 2, 'Double room with garden view', 'SUPERIEURE', 'DOUBLE', 120.00),
        (102, 1, 'Single room with city view', 'SIMPLE', 'SIMPLE', 80.00),
        (103, 2, 'Double room with balcony', 'DE_LUXE', 'DOUBLE', 150.00),
        (104, 2, 'Superb view of the lake', 'SUPERBE_VUE', 'DOUBLE', 200.00),
        (105, 1, 'Cozy single room', 'SIMPLE', 'SIMPLE', 75.00),
        (106, 3, 'Family room', 'SUPERIEURE', 'DOUBLE', 180.00),
        (107, 2, 'Double room with JACUZZI', 'DE_LUXE', 'DOUBLE', 220.00),
        (108, 1, 'Economy single room', 'SIMPLE', 'SIMPLE', 70.00),
        (109, 2, 'Superior double room', 'SUPERIEURE', 'DOUBLE', 130.00),
        (110, 2, 'Deluxe double with terrace', 'DE_LUXE', 'DOUBLE', 170.00);


INSERT INTO fidelity (id_client, value) VALUES
        (1, 100),
        (2, 50),
        (3, 75),
        (4, 120),
        (5, 30),
        (6, 80),
        (7, 200),
        (8, 60),
        (9, 90),
        (10, 40);


INSERT INTO formation (start, end, title) VALUES
        ('2025-09-01', '2025-09-03', 'Customer Service Training'),
        ('2025-09-05', '2025-09-07', 'Safety Procedures'),
        ('2025-09-08', '2025-09-10', 'Advanced Cleaning Techniques'),
        ('2025-09-11', '2025-09-13', 'Front Desk Management'),
        ('2025-09-14', '2025-09-16', 'Kitchen Hygiene'),
        ('2025-09-17', '2025-09-19', 'Conflict Resolution'),
        ('2025-09-20', '2025-09-22', 'Event Coordination'),
        ('2025-09-23', '2025-09-25', 'Hospitality Leadership'),
        ('2025-09-26', '2025-09-28', 'Luxury Room Maintenance'),
        ('2025-09-29', '2025-10-01', 'Food Safety Certification');

INSERT INTO schedule (day_of_week, shift) VALUES
        ('LUNDI', 'MATIN'),
        ('LUNDI', 'APRES_MIDI'),
        ('MARDI', 'MATIN'),
        ('MARDI', 'SOIR'),
        ('MERCREDI', 'NUIT'),
        ('JEUDI', 'MATIN'),
        ('VENDREDI', 'SOIR'),
        ('SAMEDI', 'APRES_MIDI'),
        ('DIMANCHE', 'MATIN'),
        ('DIMANCHE', 'SOIR');

INSERT INTO vacation (id_employee, start, end) VALUES
        (1, '2025-09-01', '2025-09-05'),
        (2, '2025-09-02', '2025-09-06'),
        (3, '2025-09-03', '2025-09-07'),
        (4, '2025-09-04', '2025-09-08'),
        (5, '2025-09-05', '2025-09-09'),
        (6, '2025-09-06', '2025-09-10'),
        (7, '2025-09-07', '2025-09-11'),
        (8, '2025-09-08', '2025-09-12'),
        (9, '2025-09-09', '2025-09-13'),
        (10, '2025-09-10', '2025-09-14');

INSERT INTO employee_schedule (id_employee, id_schedule) VALUES
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
        (6, 6),
        (7, 7),
        (8, 8),
        (9, 9),
        (10, 10);

INSERT INTO employee_formation (id_employee, id_formation) VALUES
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
        (6, 6),
        (7, 7),
        (8, 8),
        (9, 9),
        (10, 10);

INSERT INTO room_bonus (type, daily_price) VALUES
        ('TV', 5.00),
        ('SECHOIR', 2.00),
        ('MINI_BAR', 10.00),
        ('BOUILLOIRE', 3.00),
        ('FAUTEUIL_MASSANT', 15.00),
        ('PC_GAMING', 20.00),
        ('JACUZZI', 25.00),
        ('TV', 5.00),
        ('MINI_BAR', 10.00),
        ('FAUTEUIL_MASSANT', 15.00);

--
-- INSERT INTO room_reservation (id_client, creation, start_date, end_date, status, payment_date, total_price) VALUES
--         (1, '2025-09-01', '2025-09-05', 3, 1, '2025-09-01 12:00:00', 360.00),
--         (2, '2025-09-02', '2025-09-06', 2, 1, NULL, 160.00),
--         (1, '2025-09-01', '2025-09-05', 3, 1, '2025-09-01 12:00:00', 320.00),
--         (4, '2025-09-04', '2025-09-08', 1, 1, '2025-09-04 14:00:00', 80.00),
--         (5, '2025-09-05', '2025-09-09', 2, 0, '2025-09-05 15:30:00', 150.00),
--         (6, '2025-09-06', '2025-09-10', 3, 1, '2025-09-06 15:30:00', 360.00),
--         (1, '2025-09-01', '2025-09-05', 3, 1, '2025-09-01 12:00:00', 300.00),
--         (8, '2025-09-08', '2025-09-12', 1, 1, '2025-09-08 18:00:00', 75.00),
--         (9, '2025-09-09', '2025-09-13', 4, 1, '2025-09-09 19:00:00', 520.00),
--         (10, '2025-09-10', '2025-09-14', 2, 0, NULL, 260.00);


INSERT INTO room_reservation (id_client, creation, start_date, end_date, status, payment_date, total_price) VALUES
        (1, '2025-09-01', '2025-09-05', '2025-09-08', 1, '2025-09-01 12:00:00', null),
        (2, '2025-09-02', '2025-09-06', '2025-09-08', 1, '2025-09-02 12:30:00', null),
        (3, '2025-09-03', '2025-09-07', '2025-09-11', 0, NULL, null),
        (4, '2025-09-04', '2025-09-08', '2025-09-09', 1, '2025-09-04 14:00:00', null),
        (5, '2025-09-05', '2025-09-09', '2025-09-11', 1, '2025-09-05 15:30:00', null),
        (6, '2025-09-06', '2025-09-10', '2025-09-13', 1, '2025-09-06 16:00:00', null),
        (7, '2025-09-07', '2025-09-11', '2025-09-13', 0, NULL, null),
        (2, '2025-09-08', '2025-09-12', '2025-09-13', 1, '2025-09-08 18:00:00', null),
        (9, '2025-09-09', '2025-09-13', '2025-09-17', 1, '2025-09-09 19:00:00', null),
        (10, '2025-09-10', '2025-09-14', '2025-09-16', 1, '2025-09-10 20:00:00', null);

INSERT INTO room_link_reservation (id_room_reservation, id_room) VALUES
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
        (2, 6),
        (7, 7),
        (2, 8),
        (8, 9),
        (3, 1);

INSERT INTO room_link_bonus (id_room_bonus, id_room) VALUES
         (1, 1),
         (2, 2),
         (3, 3),
         (4, 4),
         (5, 5),
         (6, 6),
         (7, 7),
         (8, 8),
         (9, 9),
         (4, 1);

INSERT INTO place (type, capacity, hourly_price) VALUES
        ('SALLE_DE_REUNION', 20, 50.00),
        ('PISCINE', 15, 30.00),
        ('SPA', 10, 40.00),
        ('TENNIS', 4, 25.00),
        ('PLACARD_A_BALAIS', 1, 5.00),
        ('SALLE_SUR_DEMANDE', 25, 60.00),
        ('SALLE_DE_REUNION', 18, 45.00),
        ('SPA', 8, 35.00),
        ('PISCINE', 12, 32.00),
        ('TENNIS', 6, 28.00);


INSERT INTO event_reservation (id_client, event, start_date, end_date, total_price) VALUES
        (1, 'MARIAGE', '2025-10-01 12:00:00', '2025-10-01 18:00:00', 1000.00),
        (2, 'FETE', '2025-10-02 14:00:00', '2025-10-02 20:00:00', 800.00),
        (3, 'ANNIVERSAIRE', '2025-10-03 10:00:00', '2025-10-03 15:00:00', 500.00),
        (4, 'ENTERREMENT', '2025-10-04 09:00:00', '2025-10-04 13:00:00', 300.00),
        (5, 'REUNION', '2025-10-05 11:00:00', '2025-10-05 16:00:00', 700.00),
        (6, 'AUTRE', '2025-10-06 12:00:00', '2025-10-06 18:00:00', 600.00),
        (7, 'MARIAGE', '2025-10-07 14:00:00', '2025-10-07 20:00:00', 1200.00),
        (8, 'FETE', '2025-10-08 15:00:00', '2025-10-08 21:00:00', 900.00),
        (9, 'ANNIVERSAIRE', '2025-10-09 10:00:00', '2025-10-09 15:00:00', 550.00),
        (10, 'REUNION', '2025-10-10 11:00:00', '2025-10-10 16:00:00', 750.00);

INSERT INTO event_link_place (id_event_reservation, id_place) VALUES
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
        (6, 6),
        (7, 7),
        (8, 8),
        (9, 9),
        (10, 10);

INSERT INTO feedback (id_room_reservation, rate, comment) VALUES
        (1, 5, 'Excellent stay!'),
        (2, 4, 'Very good service.'),
        (3, 3, 'Average experience.'),
        (4, 2, 'Room was not clean.'),
        (5, 5, 'Loved it!'),
        (6, 4, 'Nice stay.'),
        (7, 3, 'Okay experience.'),
        (8, 5, 'Fantastic!'),
        (9, 4, 'Good overall.'),
        (10, 3, 'Could be better.');

INSERT INTO response (id_manager, id_feedback, answer) VALUES
        (1, 1, 'Thank you for your feedback!'),
        (2, 2, 'We appreciate your comment.'),
        (1, 3, 'Sorry to hear that.'),
        (2, 4, 'We will improve.'),
        (1, 5, 'Glad you enjoyed it!'),
        (2, 6, 'Thanks for your review.'),
        (1, 7, 'We will work on it.'),
        (2, 8, 'Fantastic! Thank you!'),
        (1, 9, 'We appreciate your feedback.'),
        (2, 10, 'Thank you for your suggestions.');
--
-- PAR CLIENT liste resa + prix total
-- SELECT resa.id_client,
--        resa.id_room_reservation,
--        resa.start_date,
--        resa.end_date,
--        (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
--             AS total_price
-- FROM room_reservation AS resa
--     JOIN room_link_reservation AS link
--         USING(id_room_reservation)
--     JOIN room
--         USING(id_room)
-- WHERE resa.id_client = 2
-- GROUP BY resa.id_client, resa.id_room_reservation, resa.start_date, resa.end_date;
--
-- PAR RESERVATION (client) resa + prix total
-- SELECT resa.id_client,
--        resa.id_room_reservation,
--        resa.start_date,
--        resa.end_date,
--        (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
--            AS total_price
-- FROM room_reservation AS resa
--          JOIN room_link_reservation AS link
--               USING(id_room_reservation)
--          JOIN room
--               USING(id_room)
-- WHERE resa.id_room_reservation = 2
-- GROUP BY resa.id_client, resa.id_room_reservation, resa.start_date, resa.end_date;