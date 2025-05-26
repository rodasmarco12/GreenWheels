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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Obtener la última lectura registrada para una estación")
    @ApiResponse(responseCode = "200", description = "Última lectura encontrada", content = @Content(schema = @Schema(implementation = Reading.class)))
    @PostMapping("/estacion/{id}")
    public Mono<ResponseEntity<Reading>> create(@PathVariable String id, @RequestBody Reading r) {

        return readingService.save(id, r)
                .map(saved -> ResponseEntity.status(201).body(saved))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Obtener lecturas entre dos fechas")
    @ApiResponse(responseCode = "200", description = "Lecturas encontradas", content = @Content(schema = @Schema(implementation = Reading.class)))
    @GetMapping("/estacion/{id}/status")
    public Mono<ResponseEntity<Reading>> getLastReading(@PathVariable String id) {
        return readingService.getLastReading(id)
                .map(reading -> ResponseEntity.ok(reading))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener lecturas entre dos fechas")
    @ApiResponse(responseCode = "200", description = "Lecturas encontradas", content = @Content(schema = @Schema(implementation = Reading.class)))
    @GetMapping("/estacion/{id}/lectura-status")
    public Flux<Reading> getBetween(@PathVariable String id, @RequestParam Instant from,
            @RequestParam Instant to) {
        return readingService.getBetween(id, from, to);
    }

    @Operation(summary = "Obtener todas las lecturas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de lecturas", content = @Content(schema = @Schema(implementation = Reading.class)))
    @GetMapping("/estacion/todos")
    public Flux<Reading> getAll() {
        return readingService.getAll();
    }
}
