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



//

DELIMITER ;

INSERT INTO aparcamiento (id, direction, bikesCapacity, latitude, longitude) VALUES
('1', 'aparcamiento-1', 50, 120, 880),
('2', 'aparcamiento-2', 50, 230, 670),
('3', 'aparcamiento-3', 50, 540, 520),
('4', 'aparcamiento-4', 50, 760, 300),
('5', 'aparcamiento-5', 50, 980, 450),
('6', 'aparcamiento-6', 50, 310, 120),
('7', 'aparcamiento-7', 50, 410, 780),
('8', 'aparcamiento-8', 50, 620, 640),
('9', 'aparcamiento-9', 50, 150, 220),
('10', 'aparcamiento-10', 50, 860, 190),
('11', 'aparcamiento-11', 50, 275, 900),
('12', 'aparcamiento-12', 50, 470, 740),
('13', 'aparcamiento-13', 50, 690, 110),
('14', 'aparcamiento-14', 50, 840, 510),
('15', 'aparcamiento-15', 50, 510, 630),
('16', 'aparcamiento-16', 50, 660, 360),
('17', 'aparcamiento-17', 50, 135, 470),
('18', 'aparcamiento-18', 50, 935, 820),
('19', 'aparcamiento-19', 50, 370, 260),
('20', 'aparcamiento-20', 50, 725, 390);



GRANT ALL PRIVILEGES ON bicicletas.* TO 'marcorodas'@'%';
FLUSH PRIVILEGES;