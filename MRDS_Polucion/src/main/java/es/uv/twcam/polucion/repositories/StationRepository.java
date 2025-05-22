package es.uv.twcam.polucion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uv.twcam.polucion.domain.Station;

// Repositorio de MySQL
@Repository
public interface StationRepository extends JpaRepository<Station, String> {
}
