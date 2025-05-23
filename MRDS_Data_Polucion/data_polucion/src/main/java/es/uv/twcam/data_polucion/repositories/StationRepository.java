package es.uv.twcam.data_polucion.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import es.uv.twcam.data_polucion.domain.Station;

// Repositorio de MySQL
@Repository
public interface StationRepository extends ReactiveCrudRepository<Station, String> {
}
