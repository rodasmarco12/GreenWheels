package es.uv.twcam.polucion.services;

import org.springframework.stereotype.Service;

import es.uv.twcam.polucion.domain.Station;
import es.uv.twcam.polucion.repositories.StationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
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
}
