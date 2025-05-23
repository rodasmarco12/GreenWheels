db = db.getSiblingDB('bicicletas');

db.createCollection('eventos');

const aparcamientosData = [
  { id: "1", latitude: 120, longitude: 880 },
  { id: "2", latitude: 230, longitude: 670 },
  { id: "3", latitude: 540, longitude: 520 },
  { id: "4", latitude: 760, longitude: 300 },
  { id: "5", latitude: 980, longitude: 450 },
  { id: "6", latitude: 310, longitude: 120 },
  { id: "7", latitude: 410, longitude: 780 },
  { id: "8", latitude: 620, longitude: 640 },
  { id: "9", latitude: 150, longitude: 220 },
  { id: "10", latitude: 860, longitude: 190 },
  { id: "11", latitude: 275, longitude: 900 },
  { id: "12", latitude: 470, longitude: 740 },
  { id: "13", latitude: 690, longitude: 110 },
  { id: "14", latitude: 840, longitude: 510 },
  { id: "15", latitude: 510, longitude: 630 },
  { id: "16", latitude: 660, longitude: 360 },
  { id: "17", latitude: 135, longitude: 470 },
  { id: "18", latitude: 935, longitude: 820 },
  { id: "19", latitude: 370, longitude: 260 },
  { id: "20", latitude: 725, longitude: 390 }
];

const operations = ['aparcamiento', 'alquiler', 'reposición_múltiple', 'retirada_múltiple'];

function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randomTimestamp(startDate, endDate) {
  return new Date(startDate.getTime() + Math.random() * (endDate.getTime() - startDate.getTime()));
}

const startDate = new Date('2025-05-05T00:00:00Z');
const endDate = new Date('2025-05-09T23:59:59Z');

const eventos = [];

for (let i = 0; i < 100; i++) {
  const aparcamiento = aparcamientosData[randomInt(0, aparcamientosData.length - 1)];
  const operation = operations[randomInt(0, operations.length - 1)];
  const bikesAvailable = randomInt(0, 50);
  const freeParkingSpots = 50 - bikesAvailable;
  const timestamp = randomTimestamp(startDate, endDate);

  eventos.push({
    aparcamientoId: aparcamiento.id,
    operation: operation,
    bikesAvailable: bikesAvailable,
    freeParkingSpots: freeParkingSpots,
    latitude: aparcamiento.latitude,
    longitude: aparcamiento.longitude,
    timestamp: timestamp
  });
}

db.eventos.insertMany(eventos);
