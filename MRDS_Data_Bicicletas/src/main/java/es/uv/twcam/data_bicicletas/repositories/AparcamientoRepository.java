package es.uv.twcam.data_bicicletas.repositories;


import es.uv.twcam.data_bicicletas.domain.Aparcamiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface AparcamientoRepository extends ReactiveCrudRepository<Aparcamiento, String> {

     Mono<Aparcamiento> findById(String id);
     Flux<Aparcamiento> findAll();


}
