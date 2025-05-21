package es.uv.twcam.bicicletas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uv.twcam.bicicletas.domain.Aparcamiento;

import java.util.List;
import java.util.Optional;


public interface AparcamientoRepository extends JpaRepository<Aparcamiento, String> {
    
     Optional<Aparcamiento> findById(String id);
     List<Aparcamiento> findAll();



}
