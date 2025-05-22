package es.uv.twcam.data_bicicletas.repositories;

import es.uv.twcam.data_bicicletas.domain.Evento;
import es.uv.twcam.data_bicicletas.DTO.AverageResult;
import reactor.core.publisher.Flux;

import java.time.Instant;

public interface EventoRepositoryCustom {
    Flux<Evento> findDisponibles();
    Flux<AverageResult> findAverageBikesAvailableByAparcamientoId();
    Flux<Evento> findTop10ByBikesAvailableAtTimestamp(Instant timestamp);
}
