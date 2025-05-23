package es.uv.twcam.data_polucion.repositories;

import java.time.Instant;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import es.uv.twcam.data_polucion.domain.Reading;
import reactor.core.publisher.Flux;

@Repository
public interface ReadingRepository extends ReactiveMongoRepository<Reading, String> {
    Flux<Reading> findTop1ByStationIdOrderByTimeStampDesc(String stationId);

    Flux<Reading> findByStationIdAndTimeStampBetween(String stationId, Instant from, Instant to);
}