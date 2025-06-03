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

    // Obtener estadísticas entre dos fechas ->
    // db.statistics_data.find({ timeStamp: { $gte: ISODate(start), $lte:
    // ISODate(end) } })
    public Flux<StatisticsData> getStatisticsBetween(Instant start, Instant end) {
        return sr.findByTimeStampBetween(start, end);
    }

    // Obtener la última estadística registrada ->
    // db.statistics_data.find().sort({ timeStamp: -1 }).limit(1)
    public Mono<StatisticsData> getLatestStatistics() {
        return sr.findTopByOrderByTimeStampDesc();
    }

    // Guardar una estadística agregada ->
    // db.statistics_data.insertOne({ aparcamientoId, average_bikesAvailable,
    // air_quality: { ... }, timeStamp })
    public Mono<StatisticsData> save(StatisticsData statisticsData) {
        return sr.save(statisticsData);
    }

    // Obtener todas las estadísticas ->
    // db.statistics_data.find({})
    public Flux<StatisticsData> getAllStatistics() {
        return sr.findAll();
    }

}
