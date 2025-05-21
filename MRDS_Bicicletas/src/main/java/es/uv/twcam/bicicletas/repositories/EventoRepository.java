package es.uv.twcam.bicicletas.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import es.uv.twcam.bicicletas.domain.Evento;
import java.util.List;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import es.uv.twcam.bicicletas.DTO.AverageResult;

public interface EventoRepository extends MongoRepository<Evento, String> {
   
    // Devuelve todos los eventos de un aparcamiento específico
    List<Evento> findByAparcamientoId(String aparcamientoId);

    // Devuelve el último evento de un aparcamiento en específico
    Optional<Evento> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId);

    // Devuelve los eventos de un aparcamiento específico entre dos fechas
    List<Evento> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);

    // Devuelve los aparcamientos con bicis disponibles
    @Aggregation(pipeline = {
        "{ $sort: { aparcamientoId: 1, timestamp: -1 } }",
        "{ $group: { _id: \"$aparcamientoId\", evento: { $first: \"$$ROOT\" } } }",
        "{ $replaceRoot: { newRoot: \"$evento\" } }",
        "{ $match: { bikesAvailable: { $gt: 0 } } }"
    })
    List<Evento> findDisponibles();

    // Devuelve los aparcamientos junto con promedio de bicis disponibles, latitud y longitud
    @Aggregation(pipeline = {
        "{ $group: { _id: \"$aparcamientoId\", avgBikesAvailable: { $avg: \"$bikesAvailable\" }, latitude: { $first: \"$latitude\" }, longitude: { $first: \"$longitude\" } } }"
    })
    List<AverageResult> findAverageBikesAvailableByAparcamientoId();


    //Encuentra los 10 aparcamientos con mayor numero de bicis disponibles en un momento específico
    @Aggregation(pipeline = {
        "{ $match: { timestamp: ?0 } }",
        "{ $sort: { bikesAvailable: -1 } }",
        "{ $limit: 10 }"
    })
    List<Evento> findTop10ByBikesAvailableAtTimestamp(Instant timestamp);


    // Encuentra los eventos de un aparcamiento específico entre dos fechas
    @Query("{ 'aparcamientoId': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<Evento> findEstadoChangesByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);






    
}
