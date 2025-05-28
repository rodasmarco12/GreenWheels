package es.uv.twcam.data_ayun.repositories;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import es.uv.twcam.data_ayun.domain.StatisticsData;

import java.time.Instant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StatisticsRepository extends ReactiveMongoRepository<StatisticsData, String> {

    Flux<StatisticsData> findByTimeStampBetween(Instant start, Instant end);

    // 
    Mono<StatisticsData> findTopByOrderByTimeStampDesc();

    Flux<StatisticsData> findAll();

}
