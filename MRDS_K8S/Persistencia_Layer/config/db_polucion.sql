CREATE DATABASE IF NOT EXISTS polucion;
USE polucion;

CREATE TABLE IF NOT EXISTS station (
    id CHAR(36) PRIMARY KEY,
    direction VARCHAR(255) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL
);

DELIMITER //

-- CREATE TRIGGER before_insert_station
-- BEFORE INSERT ON station
-- FOR EACH ROW
-- BEGIN
--     IF NEW.id IS NULL THEN
--         SET NEW.id = UUID();
--     END IF;
-- END;

-- //

DELIMITER ;

INSERT INTO station (id, direction, latitude, longitude) VALUES
('1', 'station-1', 100, 95),
('2', 'station-2', 340, 205),
('3', 'station-3', 760, 365),
('4', 'station-4', 180, 485),
('5', 'station-5', 610, 580),
('6', 'station-6', 270, 645),
('7', 'station-7', 820, 715),
('8', 'station-8', 490, 805),
('9', 'station-9', 150, 875),
('10', 'station-10', 700, 960);



GRANT ALL PRIVILEGES ON polucion.* TO 'marcorodas'@'%';
FLUSH PRIVILEGES;

