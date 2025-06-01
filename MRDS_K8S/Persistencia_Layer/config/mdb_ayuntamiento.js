const randomFloat = (min, max) => +(Math.random() * (max - min) + min).toFixed(2);

function generateDocument(date, id) {
  const aggregatedData = [];

  for (let i = 1; i <= 20; i++) {
    aggregatedData.push({
      aparcamientoId: String(i),
      average_bikesAvailable: randomFloat(0, 20),
      air_quality: {
        nitricOxides: randomFloat(5, 20),
        nitrogenDioxides: randomFloat(1, 10),
        VOCs_NMHC: randomFloat(2, 25),
        PM2_5: randomFloat(0, 15)
      }
    });
  }

  return {
    _id: id,
    timeStamp: date.toISOString(),
    aggregatedData: aggregatedData
  };
}

// Conexión a la base de datos
db = db.getSiblingDB("ayuntamiento");

// Eliminar colección si ya existe (opcional)
db.statistics_data.drop();

// Crear colección explícitamente
db.createCollection("statistics_data");

const documents = [];
const startDate = new Date("2025-05-26T12:00:00Z");

for (let i = 1; i <= 5; i++) {
  const date = new Date(startDate);
  date.setDate(startDate.getDate() + (i - 1));
  documents.push(generateDocument(date, i));
}

db.statistics_data.insertMany(documents);
