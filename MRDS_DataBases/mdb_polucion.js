

db = db.getSiblingDB('polucion');

db.createCollection('pollution_readings');

const estaciones = [
  { id: "1", latitude: 100, longitude: 95 },
  { id: "2", latitude: 340, longitude: 205 },
  { id: "3", latitude: 760, longitude: 365 },
  { id: "4", latitude: 180, longitude: 485 },
  { id: "5", latitude: 610, longitude: 580 },
  { id: "6", latitude: 270, longitude: 645 },
  { id: "7", latitude: 820, longitude: 715 },
  { id: "8", latitude: 490, longitude: 805 },
  { id: "9", latitude: 150, longitude: 875 },
  { id: "10", latitude: 700, longitude: 960 }
];

function randomFloat(min, max) {
  return parseFloat((Math.random() * (max - min) + min).toFixed(2));
}

function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randomTimestamp(start, end) {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
}

const startDate = new Date('2025-05-05T00:00:00Z');
const endDate = new Date('2025-05-09T23:59:59Z');

const readings = [];

for (let i = 0; i < 100; i++) {
  const estacion = estaciones[randomInt(0, estaciones.length - 1)];
  const timestamp = randomTimestamp(startDate, endDate);

  readings.push({
    stationId: estacion.id,
    timeStamp: timestamp,
    nitricOxides: randomFloat(1, 100),
    nitrogenDioxides: randomFloat(1, 100),
    VOCs_NMHC: randomFloat(1, 100),
    PM2_5: randomFloat(1, 100),
    longitude: estacion.longitude,
    latitude: estacion.latitude
  });
}

db.pollution_readings.insertMany(readings);
