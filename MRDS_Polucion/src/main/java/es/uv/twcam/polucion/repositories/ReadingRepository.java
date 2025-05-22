package es.uv.twcam.polucion.repositories;

import java.time.Instant;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import es.uv.twcam.polucion.domain.Reading;
import reactor.core.publisher.Flux;

@Repository
public interface ReadingRepository extends ReactiveMongoRepository<Reading, String> {
    Flux<Reading> findTop1ByStationIdOderByTimeStampDesc(String stationId);

    Flux<Reading> findByStationIdAndTimeStampBetween(String stationId, Instant from, Instant to);
}
