package es.uv.twcam.data_ayun.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.uv.twcam.data_ayun.repositories.StatisticsRepository;
import es.uv.twcam.data_ayun.domain.StatisticsData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository sr;


    public Flux<StatisticsData> getStatisticsBetween(Instant start, Instant end) {
        return sr.findByTimeStampBetween(start, end);
    }
    public Mono<StatisticsData> getLatestStatistics() {
        return sr.findTopByOrderByTimeStampDesc();
    }

    public Mono<StatisticsData> save(StatisticsData statisticsData) {
        return sr.save(statisticsData);
    }

    public Flux<StatisticsData> getAllStatistics() {
        return sr.findAll();
    }

}
