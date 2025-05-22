package es.uv.twcam.data_bicicletas.controllers;

import java.time.Instant;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uv.twcam.data_bicicletas.DTO.AverageResult;
import es.uv.twcam.data_bicicletas.domain.Aparcamiento;
import es.uv.twcam.data_bicicletas.domain.Evento;
import es.uv.twcam.data_bicicletas.services.AparcamientoService;
import es.uv.twcam.data_bicicletas.services.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data/bicicletas")
@CrossOrigin
public class DataBicicletasController {
    @Autowired
    private AparcamientoService aparcamientoService;

    @Autowired
    private EventoService eventoService;

    // APARCAMIENTOS ENDPOINTS

    @Operation(summary = "Get all parkings")
    @GetMapping("/aparcamiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parkings found"),
            @ApiResponse(responseCode = "404", description = "No parkings found")
    })
    public Mono<ResponseEntity<Flux<Aparcamiento>>> getAllAparcamientos() {
        return Mono.just(ResponseEntity.ok(aparcamientoService.findAll()));
    }

    @Operation(summary = "Get a parking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking found"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    @GetMapping("/aparcamiento/{id}")
    public Mono<ResponseEntity<Aparcamiento>> getAparcamientoById(@PathVariable String id) {
        return aparcamientoService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new parking")
    @PostMapping("/aparcamiento")
    public Mono<ResponseEntity<Aparcamiento>> createAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        return aparcamientoService.save(aparcamiento)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Update an existing parking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking updated"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    @PutMapping("/aparcamiento/{id}")
    public Mono<ResponseEntity<Aparcamiento>> updateAparcamiento(@PathVariable String id,
            @RequestBody Aparcamiento aparcamiento) {
        return aparcamientoService.update(id, aparcamiento)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a parking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Parking deleted"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    @DeleteMapping("/aparcamiento/{id}")
    public Mono<ResponseEntity<Void>> deleteAparcamiento(@PathVariable String id) {
        return aparcamientoService.findById(id)
                .flatMap(existing -> aparcamientoService.deleteById(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    // EVENTOS ENDPOINTS


    @Operation(summary = "Get all events for a parking")
    @GetMapping("/evento/aparcamiento/{id}")
    public Flux<Evento> getEventosByAparcamiento(@PathVariable String id) {
        return eventoService.getEventosByAparcamiento(id);
    }

    @Operation(summary = "Get current status for a parking")
    @GetMapping("/evento/status/{id}")
    public Mono<ResponseEntity<Evento>> getStatus(@PathVariable String id) {
        return eventoService.getStatus(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get events between two dates")
    @GetMapping("/evento/entre-fechas")
    public Flux<Evento> getEventosEntreFechas(
            @RequestParam String aparcamientoId,
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return eventoService.getEventosEntreFechas(aparcamientoId, inicio, fin);
    }

    @Operation(summary = "Get state changes between two dates")
    @GetMapping("/evento/cambios-estado")
    public Flux<Evento> getCambiosEstado(
            @RequestParam String aparcamientoId,
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return eventoService.getCambiosEstado(aparcamientoId, inicio, fin);
    }

    @Operation(summary = "Get available parkings with bikes")
    @GetMapping("/evento/disponibles")
    public Flux<Evento> getDisponibles() {
        return eventoService.getDisponibles();
    }

    @Operation(summary = "Get average bikes available per parking")
    @GetMapping("/evento/promedios")
    public Flux<AverageResult> getPromedioBicisPorAparcamiento() {
        return eventoService.getPromedioBicisPorAparcamiento();
    }

    @Operation(summary = "Get top 10 parkings with most bikes at a given time")
    @GetMapping("/evento/top10")
    public Flux<Evento> getTop10(@RequestParam Instant timestamp) {
        return eventoService.getTop10PorDisponibilidad(timestamp);
    }

    @Operation(summary = "Get closest available parking to given coordinates")
    @GetMapping("/evento/disponible-cercano")
    public Mono<ResponseEntity<Evento>> getDisponibleCercano(
            @RequestParam float latitud,
            @RequestParam float longitud) {
        return eventoService.getDisponibleByLocation(latitud, longitud)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new event")
    @PostMapping("/evento")
    public Mono<ResponseEntity<Evento>> save(@RequestBody Evento evento) {
        return eventoService.save(evento)
                .map(saved -> ResponseEntity.ok(saved));
    }

    @Operation(summary = "Delete an event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @DeleteMapping("/evento/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return eventoService.getStatus(id)
                .flatMap(existing -> eventoService.deleteById(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
