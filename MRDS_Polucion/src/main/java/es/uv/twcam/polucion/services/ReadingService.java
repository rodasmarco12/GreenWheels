package es.uv.twcam.polucion.services;

import java.time.Instant;

import org.springframework.stereotype.Service;

import es.uv.twcam.polucion.domain.Reading;
import es.uv.twcam.polucion.repositories.ReadingRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReadingService {
    private final ReadingRepository readingRepository;

    public Mono<Reading> save(Reading r) {
        return readingRepository.save(r);
    }

    public Mono<Reading> getLastReading(String stationId) {
        return readingRepository.findTop1ByStationIdOrderByTimeStampDesc(stationId).next();
    }

    public Flux<Reading> getBetween(String stationId, Instant from, Instant to) {
        return readingRepository.findByStationIdAndTimeStampBetween(stationId, from, to);
    }
}
