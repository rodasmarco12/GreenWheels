package es.uv.twcam.data_polucion.services;

import org.springframework.stereotype.Service;

import es.uv.twcam.data_polucion.domain.Station;
import es.uv.twcam.data_polucion.repositories.StationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // Obtener todas las estaciones -> SELECT * FROM station;
    public Flux<Station> findAll() {
        return stationRepository.findAll();
    }

    // Guardar una estación -> INSERT INTO station (id, direction, latitude, longitude) VALUES (?, ?, ?, ?);
    public Mono<Station> save(Station s) {
        return stationRepository.save(s);
    }

    // Eliminar por id -> DELETE FROM station WHERE id = ?;
    public Mono<Void> delete(String id) {
        return stationRepository.deleteById(id);
    }

    // Buscar por id -> SELECT * FROM station WHERE id = ?;
    public Mono<Station> findById(String id) {
        return stationRepository.findById(id);
    }

    // Actualizar una estación -> UPDATE station SET direction = ?, latitude = ?, longitude = ? WHERE id = ?;
    public Mono<Station> update(String id, Station s) {
        return this.stationRepository.findById(id)
                .flatMap(station -> {
                    station.setDirection(s.getDirection());
                    station.setLatitude(s.getLatitude());
                    station.setLongitude(s.getLongitude());
                    return stationRepository.save(station);
                });
    }
}
