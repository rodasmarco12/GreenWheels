package es.uv.twcam.data_polucion.services;

import java.util.UUID;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import es.uv.twcam.data_polucion.domain.Station;
import es.uv.twcam.data_polucion.repositories.StationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StationService {
    private final StationRepository stationRepository;

    private final DatabaseClient databaseClient;

    public StationService(StationRepository stationRepository, DatabaseClient databaseClient) {
        this.stationRepository = stationRepository;
        this.databaseClient = databaseClient;
    }

    // Obtener todas las estaciones
    public Flux<Station> findAll() {
        return stationRepository.findAll();
    }

    // Guardar una estación
    public Mono<Station> save(Station s) {
        return stationRepository.save(s);
    }

    // Eliminar por id
    public Mono<Void> delete(String id) {
        return stationRepository.deleteById(id);
    }

    // Buscar por id
    public Mono<Station> findById(String id) {
        return stationRepository.findById(id);
    }

    public Mono<Station> update(String id, Station s) {
        return this.stationRepository.findById(id)
                .flatMap(station -> {
                    station.setDirection(s.getDirection());
                    station.setLatitude(s.getLatitude());
                    station.setLongitude(s.getLongitude());
                    return stationRepository.save(station);
                });
    }


    public Mono<Station> insertStation(Station s) {
        // Generar UUID si no existe
        if (s.getId() == null || s.getId().isEmpty()) {
            s.setId(UUID.randomUUID().toString());
        }

        // Hacer insert con DatabaseClient
        return databaseClient.sql("INSERT INTO station (id, direction, latitude, longitude) " +
                "VALUES (:id, :direction, :latitude, :longitude)")
            .bind("id", s.getId())
            .bind("direction", s.getDirection())
           
            .bind("latitude", s.getLatitude())
            .bind("longitude", s.getLongitude())
            .then() // solo indica que termina la inserción, no devuelve el objeto
            .thenReturn(s); // retornamos el objeto con el ID ya seteado
    }






}
