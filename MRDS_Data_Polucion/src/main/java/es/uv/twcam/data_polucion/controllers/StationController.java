package es.uv.twcam.data_polucion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.twcam.data_polucion.domain.Station;
import es.uv.twcam.data_polucion.services.StationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data-polucion")
@CrossOrigin
@RequiredArgsConstructor
public class StationController {

    @Autowired
    private StationService stationService;

    @PostMapping("/estacion")
    public Mono<ResponseEntity<Station>> create(@RequestBody Station s) {
        return stationService.save(s)
                .map(saved -> ResponseEntity.status(201).body(saved))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/estacion/update/{id}")
    public Mono<ResponseEntity<Station>> update(@PathVariable String id, @RequestBody Station s) {
        return this.stationService.update(id, s)
                .map(updated -> ResponseEntity.ok(updated))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/estacion/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return stationService.delete(id)
                .flatMap(station -> {
                    if (station != null) {
                        return stationService.delete(id)
                                .then(Mono.just(ResponseEntity.noContent().build()));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @GetMapping("/estaciones")
    public Flux<Station> getAll() {
        return stationService.findAll();
    }

}
