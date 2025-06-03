package es.uv.twcam.data_polucion.services;

import java.time.Instant;

import org.springframework.stereotype.Service;

import es.uv.twcam.data_polucion.domain.Reading;
import es.uv.twcam.data_polucion.repositories.ReadingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReadingService {
    private final ReadingRepository readingRepository;

    // Guardar una lectura -> db.pollution_readings.insertOne({ stationId,
    // timeStamp, nitricOxides, nitrogenDioxides, VOCs_NMHC, PM2_5 })
    public Mono<Reading> save(String id, Reading r) {
        return readingRepository.save(r)
                .flatMap(reading -> {
                    reading.setStationId(id);
                    return readingRepository.save(reading);
                });
    }

    // Obtener la última lectura de una estación -> db.pollution_readings.find({
    // stationId }).sort({ timeStamp: -1 }).limit(1)
    public Mono<Reading> getLastReading(String stationId) {
        return readingRepository.findTop1ByStationIdOrderByTimeStampDesc(stationId).next();
    }

    // Obtener lecturas entre dos fechas -> db.pollution_readings.find({ stationId,
    // timeStamp: { $gte: from, $lte: to } })
    public Flux<Reading> getBetween(String stationId, Instant from, Instant to) {
        return readingRepository.findByStationIdAndTimeStampBetween(stationId, from, to);
    }

    // Obtener todas las lecturas -> db.pollution_readings.find({})
    public Flux<Reading> getAll() {
        return readingRepository.findAll();
    }
}
