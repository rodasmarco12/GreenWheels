CREATE DATABASE IF NOT EXISTS bicicletas;
USE bicicletas;

CREATE TABLE IF NOT EXISTS aparcamiento (
    id CHAR(36) PRIMARY KEY,
    direction VARCHAR(255) NOT NULL,
    bikesCapacity INT NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL
);

DELIMITER //

CREATE TRIGGER before_insert_aparcamiento
BEFORE INSERT ON aparcamiento
FOR EACH ROW
BEGIN
    IF NEW.id IS NULL THEN
        SET NEW.id = UUID();
    END IF;
END;

//

DELIMITER ;

INSERT INTO aparcamiento (id, direction, bikesCapacity, latitude, longitude) VALUES
('1', 'estacion-1', 50, 120, 880),
('2', 'estacion-2', 50, 230, 670),
('3', 'estacion-3', 50, 540, 520),
('4', 'estacion-4', 50, 760, 300),
('5', 'estacion-5', 50, 980, 450),
('6', 'estacion-6', 50, 310, 120),
('7', 'estacion-7', 50, 410, 780),
('8', 'estacion-8', 50, 620, 640),
('9', 'estacion-9', 50, 150, 220),
('10', 'estacion-10', 50, 860, 190),
('11', 'estacion-11', 50, 275, 900),
('12', 'estacion-12', 50, 470, 740),
('13', 'estacion-13', 50, 690, 110),
('14', 'estacion-14', 50, 840, 510),
('15', 'estacion-15', 50, 510, 630),
('16', 'estacion-16', 50, 660, 360),
('17', 'estacion-17', 50, 135, 470),
('18', 'estacion-18', 50, 935, 820),
('19', 'estacion-19', 50, 370, 260),
('20', 'estacion-20', 50, 725, 390);
