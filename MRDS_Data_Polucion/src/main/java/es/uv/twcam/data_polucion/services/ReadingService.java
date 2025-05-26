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

    public Mono<Reading> save(String id, Reading r) {
        return readingRepository.save(r)
                .flatMap(reading -> {
                    reading.setStationId(id);
                    return readingRepository.save(reading);
                });
    }

    public Mono<Reading> getLastReading(String stationId) {
        return readingRepository.findTop1ByStationIdOrderByTimeStampDesc(stationId).next();
    }

    public Flux<Reading> getBetween(String stationId, Instant from, Instant to) {
        return readingRepository.findByStationIdAndTimeStampBetween(stationId, from, to);
    }

    public Flux<Reading> getAll() {
        return readingRepository.findAll();
    }
}
