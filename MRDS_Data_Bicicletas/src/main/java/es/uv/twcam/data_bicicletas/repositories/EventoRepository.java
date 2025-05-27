package es.uv.twcam.data_bicicletas.repositories;
import es.uv.twcam.data_bicicletas.domain.Evento;




import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.time.Instant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface EventoRepository extends ReactiveMongoRepository<Evento, String>, EventoRepositoryCustom {
   
    // Devuelve todos los eventos de un aparcamiento específico
    Flux<Evento> findByAparcamientoId(String aparcamientoId);

    // Devuelve el último evento de un aparcamiento en específico
    Mono<Evento> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId);

    // Devuelve los eventos de un aparcamiento específico entre dos fechas
    Flux<Evento> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);


    // Encuentra los eventos de un aparcamiento específico entre dos fechas
    @Query("{ 'aparcamientoId': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    Flux<Evento> findEstadoChangesByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);


 
}
