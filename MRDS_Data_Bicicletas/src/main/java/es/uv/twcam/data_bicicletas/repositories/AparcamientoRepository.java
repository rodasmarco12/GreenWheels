package es.uv.twcam.data_bicicletas.repositories;

import es.uv.twcam.data_bicicletas.domain.Aparcamiento;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.r2dbc.repository.Query;

public interface AparcamientoRepository extends ReactiveCrudRepository<Aparcamiento, String> {

     Mono<Aparcamiento> findById(String id);

     Flux<Aparcamiento> findAll();

     // @Modifying
     // @Query("INSERT INTO aparcamiento (id, direction, bikesCapacity, latitude, longitude) VALUES (:#{#a.id}, :#{#a.direction}, :#{#a.bikesCapacity}, :#{#a.latitude}, :#{#a.longitude})")
     // Mono<Aparcamiento> insertAparcamiento(@Param("a") Aparcamiento aparcamiento);


     Mono<Aparcamiento> save(Aparcamiento aparcamiento);

     Mono<Void> deleteById(String id);

}
