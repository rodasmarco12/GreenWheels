package es.uv.twcam.data_bicicletas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.uv.twcam.data_bicicletas.domain.Aparcamiento;
import es.uv.twcam.data_bicicletas.repositories.AparcamientoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AparcamientoService {

    @Autowired
    private AparcamientoRepository ar;

    public AparcamientoService() {
    }

    public Mono<Aparcamiento> findById(String id) {
        return ar.findById(id);
    }

    // Requerimiento: Se puede obtener un listado de todos los aparcamientos.
    public Flux<Aparcamiento> findAll() {
        return ar.findAll();
    }

    // Requerimiento: Se pueden añadir aparcamientos. Un aparcamiento tiene un ID, está en una dirección, puede
    // alojar un número máximo de bicicletas, y tiene datos de geolocalización (latitud y longitud).
    public Mono<Aparcamiento> save(Aparcamiento aparcamiento) {
        return ar.save(aparcamiento);
    }


    // Requerimiento: Se puede eliminar un aparcamiento. 
    public Mono<Void> deleteById(String id) {
        return ar.deleteById(id);
    }

    public Mono<Aparcamiento> update(String id, Aparcamiento aparcamiento) {
        return ar.findById(id)
                .flatMap(existingAparcamiento -> {
                    existingAparcamiento.setDirection(aparcamiento.getDirection());
                    existingAparcamiento.setBikesCapacity(aparcamiento.getBikesCapacity());
                    existingAparcamiento.setLatitude(aparcamiento.getLatitude());
                    existingAparcamiento.setLongitude(aparcamiento.getLongitude());
                    return ar.save(existingAparcamiento);
                });
    }
}
