package es.uv.twcam.bicicletas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import es.uv.twcam.bicicletas.domain.Aparcamiento;
import es.uv.twcam.bicicletas.repositories.AparcamientoRepository;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class AparcamientoService {
    // Implement the methods to interact with the AparcamientoRepository
    // and perform business logic related to parking lots.
    // Implemented methods to find, save, update, or delete parking lots.
    
 
    
    @Autowired
    private AparcamientoRepository ar;

    public AparcamientoService() {
    }

    public Optional<Aparcamiento> findById(String id) {
        return ar.findById(id);
    }
    public List<Aparcamiento> findAll() {
        return ar.findAll();
    }
    public Aparcamiento save(Aparcamiento aparcamiento) {
        return ar.save(aparcamiento);
    }
    public void deleteById(String id) {
        ar.deleteById(id);
    }
}
