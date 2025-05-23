
CREATE TABLE IF NOT EXISTS polucion (
    id VARCHAR(10) PRIMARY KEY,
    direcction VARCHAR(100),
    latitud FLOAT,
    longitud FLOAT
);

DELIMITER //

CREATE TRIGER before_insert_polution
BEFORE INSERT ON polucion
FOR EACH ROW
BEGIN
    IF NEW.id IS NULL THEN
        SET NEW.id = UUID();
    END IF;
END;

//

DELIMITER ;

INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL004', 'Rambla Vanesa Posada 90', 39.458668, -0.396366);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL005', 'Urbanización Aurelio Mata 35', 39.453453, -0.368582);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL006', 'Rambla de Benjamín Palomo 4', 39.476046, -0.371169);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL007', 'Via Ruperto Silva 340', 39.467213, -0.346983);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL008', 'Urbanización Aristides Castrillo 52 Puerta 6 ', 39.451196, -0.331062);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL009', 'Via de Gisela Milla 4 Apt. 32 ', 39.464355, -0.396924);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL010', 'Callejón de Paulino Cañizares 5 Piso 2 ', 39.465333, -0.335622);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL011', 'Callejón Andrés Felipe Campo 98 Puerta 2 ', 39.484209, -0.378525);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL012', 'Vial de Silvestre Maza 67', 39.472064, -0.380794);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL013', 'Pasaje Javier Camino 602 Apt. 27 ', 39.474901, -0.374171);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL014', 'Alameda Marcos Mulet 35 Piso 1 ', 39.473143, -0.339151);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL015', 'Camino de Mónica Donaire 2 Piso 9 ', 39.470716, -0.392873);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL016', 'Camino Raimundo Carballo 8', 39.456391, -0.383605);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL017', 'Callejón de Maximino Bonilla 3 Puerta 1 ', 39.472026, -0.370519);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL018', 'Cañada de Timoteo Pallarès 22 Piso 3 ', 39.465076, -0.385314);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL019', 'Calle de Florentina Perera 34', 39.471435, -0.330221);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL020', 'C. Tristán Luís 288 Apt. 31 ', 39.451419, -0.369573);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL021', 'Camino de Benigno Agustí 6 Apt. 92 ', 39.480223, -0.367322);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL022', 'Ronda Mirta Garmendia 17', 39.465652, -0.384629);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL023', 'Acceso Esperanza Jordán 5', 39.484648, -0.346702);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL024', 'Via de Edu Folch 41 Apt. 17 ', 39.463091, -0.370478);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL025', 'Cuesta Samanta Morante 157 Piso 5 ', 39.468986, -0.371253);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL026', 'Pasadizo de Cándido Real 7', 39.468706, -0.364655);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL027', 'Callejón de Faustino Romeu 56 Puerta 0 ', 39.47436, -0.399702);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL028', 'Cañada de Román Amaya 256 Piso 1 ', 39.455731, -0.333831);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL029', 'Alameda Eliana Collado 16 Piso 5 ', 39.456829, -0.398179);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL030', 'Camino de Marcia Royo 23', 39.487763, -0.350199);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL031', 'Camino Aurora Albero 77 Piso 2 ', 39.469655, -0.377719);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL032', 'Cañada de Jenaro Maestre 767', 39.482273, -0.387761);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL033', 'Cañada de Rosalía Ojeda 45', 39.457948, -0.343317);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL034', 'Urbanización Estefanía Clemente 29 Puerta 3 ', 39.465477, -0.380345);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL035', 'Cañada de Ximena Ortega 8 Piso 7 ', 39.48388, -0.38142);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL036', 'Camino de Ezequiel Gallardo 382 Puerta 4 ', 39.471525, -0.391344);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL037', 'Callejón de Eliseo Alfonso 35 Puerta 2 ', 39.489034, -0.352474);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL038', 'Rambla de Sandalio Acero 86', 39.481032, -0.373967);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL039', 'Via Jesusa Navarro 16', 39.486662, -0.354093);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL040', 'Callejón Xavier Guardia 35', 39.476052, -0.397527);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL041', 'Pasadizo de Bonifacio Páez 264 Apt. 16 ', 39.473086, -0.357753);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL042', 'Acceso Onofre Carreño 40', 39.478872, -0.399845);
INSERT INTO polucion (id, direcction, latitud, longitud) VALUES ('POL043', 'Via de Florencio Araujo 479 Puerta 2 ', 39.479844, -0.364559);