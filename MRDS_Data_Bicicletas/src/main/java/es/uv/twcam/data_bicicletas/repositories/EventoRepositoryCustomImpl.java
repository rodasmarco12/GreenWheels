package es.uv.twcam.data_bicicletas.repositories;

import es.uv.twcam.data_bicicletas.domain.Evento;
import es.uv.twcam.data_bicicletas.DTO.AverageResult;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Component
public class EventoRepositoryCustomImpl implements EventoRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    public EventoRepositoryCustomImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Evento> findDisponibles() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by("aparcamientoId").ascending().and(Sort.by("timestamp").descending())),
                Aggregation.group("aparcamientoId").first("$$ROOT").as("eventos"),
                Aggregation.replaceRoot("eventos"),
                Aggregation.match(Criteria.where("bikesAvailable").gt(0)));
        return mongoTemplate.aggregate(aggregation, "eventos", Evento.class);
    }

    @Override
    public Flux<AverageResult> findAverageBikesAvailableByAparcamientoId() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("aparcamientoId")
                        .avg("bikesAvailable").as("avgBikesAvailable")
                        .first("latitude").as("latitude")
                        .first("longitude").as("longitude"));

        return mongoTemplate.aggregate(aggregation, "eventos", AverageResult.class);
    }

    
 @Override
public Flux<Evento> findTop10ByBikesAvailableAtTimestamp(Instant timestamp) {
    Aggregation aggregation = Aggregation.newAggregation(
        // 1. Filtrar eventos anteriores al timestamp
        Aggregation.match(Criteria.where("timestamp").lte(timestamp)),

        // 2. Ordenar por aparcamientoId ASC y timestamp DESC
        Aggregation.sort(Sort.by(
            Sort.Order.asc("aparcamientoId"),
            Sort.Order.desc("timestamp")
        )),

        // 3. Agrupar por aparcamientoId, tomar el evento más reciente
        Aggregation.group("aparcamientoId").first("$$ROOT").as("evento"),

        // 4. Reemplazar root para recuperar el documento original
        Aggregation.replaceRoot("evento"),

        // 5. Ordenar por bikesAvailable DESC
        Aggregation.sort(Sort.by(Sort.Order.desc("bikesAvailable"))),

        // 6. Limitar a los top 10
        Aggregation.limit(10)
    );

    return mongoTemplate.aggregate(aggregation, "eventos", Evento.class);
}



}