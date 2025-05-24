package es.uv.twcam.data_polucion.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uv.twcam.data_polucion.domain.Reading;
import es.uv.twcam.data_polucion.services.ReadingService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data-polucion")
@CrossOrigin
@RequiredArgsConstructor
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @PostMapping("/estacion/{id}")
    public Mono<ResponseEntity<Reading>> create(@PathVariable String id, @RequestBody Reading r) {

        return readingService.save(id, r)
                .map(saved -> ResponseEntity.status(201).body(saved))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/estacion/{id}/status")
    public Mono<ResponseEntity<Reading>> getLastReading(@PathVariable String id) {
        return readingService.getLastReading(id)
                .map(reading -> ResponseEntity.ok(reading))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/estacion/{id}/lectura-status")
    public Flux<Reading> getBetween(@PathVariable String id, @RequestParam Instant from,
            @RequestParam Instant to) {
        return readingService.getBetween(id, from, to);
    }

    @GetMapping("/estacion/todos")
    public Flux<Reading> getAll() {
        return readingService.getAll();
    }
}
