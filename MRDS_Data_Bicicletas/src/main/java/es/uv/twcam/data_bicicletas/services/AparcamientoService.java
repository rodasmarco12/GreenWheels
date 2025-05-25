package es.uv.twcam.data_bicicletas.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import es.uv.twcam.data_bicicletas.DTO.AparcamientoDTO;
import es.uv.twcam.data_bicicletas.domain.Aparcamiento;
import es.uv.twcam.data_bicicletas.repositories.AparcamientoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
public class AparcamientoService {

    @Autowired
    private AparcamientoRepository ar;

    private final DatabaseClient databaseClient;

    public AparcamientoService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    

    public Mono<Aparcamiento> findById(String id) {
        return ar.findById(id);
    }

    // Requerimiento: Se puede obtener un listado de todos los aparcamientos.
    public Flux<Aparcamiento> findAll() {
        return ar.findAll();
    }

    public Mono<Aparcamiento> insertAparcamiento(Aparcamiento a) {
        // Generar UUID si no existe
        if (a.getId() == null || a.getId().isEmpty()) {
            a.setId(UUID.randomUUID().toString());
        }

        // Hacer insert con DatabaseClient
        return databaseClient.sql("INSERT INTO aparcamiento (id, direction, bikesCapacity, latitude, longitude) " +
                "VALUES (:id, :direction, :bikesCapacity, :latitude, :longitude)")
            .bind("id", a.getId())
            .bind("direction", a.getDirection())
            .bind("bikesCapacity", a.getBikesCapacity())
            .bind("latitude", a.getLatitude())
            .bind("longitude", a.getLongitude())
            .then() // solo indica que termina la inserción, no devuelve el objeto
            .thenReturn(a); // retornamos el objeto con el ID ya seteado
    }



    // Requerimiento: Se pueden añadir aparcamientos. Un aparcamiento tiene un ID, está en una dirección, puede
    // alojar un número máximo de bicicletas, y tiene datos de geolocalización (latitud y longitud).
    public Mono<Aparcamiento> save(Aparcamiento aparcamiento) {
        if (aparcamiento.getId() == null) {
        aparcamiento.setId(UUID.randomUUID().toString());
    }
    return ar.save(aparcamiento);
    }


    // Requerimiento: Se puede eliminar un aparcamiento. 
    public Mono<Void> deleteById(String id) {
        return ar.deleteById(id);
    }

    public Mono<Aparcamiento> update(String id, AparcamientoDTO aparcamiento) {
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
