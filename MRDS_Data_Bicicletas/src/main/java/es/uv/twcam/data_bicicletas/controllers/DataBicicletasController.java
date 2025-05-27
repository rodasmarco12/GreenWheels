package es.uv.twcam.data_bicicletas.controllers;

import java.net.URI;
import java.time.Instant;

import java.util.UUID;

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
import es.uv.twcam.data_bicicletas.DTO.AparcamientoDTO;

@RestController
@RequestMapping("/api/v1/data/bicicletas")
@CrossOrigin

public class DataBicicletasController {
        @Autowired
        private AparcamientoService aparcamientoService;

        @Autowired
        private EventoService eventoService;

        // APARCAMIENTOS ENDPOINTS

        @Operation(summary = "R4 - Listar aparcamientos")
        @GetMapping("/aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parkings found"),
                        @ApiResponse(responseCode = "404", description = "No parkings found")
        })
        public Mono<ResponseEntity<Flux<Aparcamiento>>> getAllAparcamientos() {
                return Mono.just(ResponseEntity.ok(aparcamientoService.findAll()));
        }

        @Operation(summary = "Obtener aparcamiento por ID")
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

        @Operation(summary = "R1 - Añadir aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Aparcamiento creado"),
                        @ApiResponse(responseCode = "400", description = "Datos de aparcamiento inválidos")
        })
        @PostMapping("/aparcamiento")
        public Mono<ResponseEntity<Aparcamiento>> createAparcamiento(
                        @RequestBody AparcamientoDTO dto) {
        
                if (dto.getBikesCapacity() < 0) {
                        return Mono.just(ResponseEntity.badRequest().build());
                }

                Aparcamiento aparcamiento = new Aparcamiento();
                aparcamiento.setId(UUID.randomUUID().toString());
                aparcamiento.setDirection(dto.getDirection());
                aparcamiento.setBikesCapacity(dto.getBikesCapacity());
                aparcamiento.setLatitude(dto.getLatitude());
                aparcamiento.setLongitude(dto.getLongitude());

                return aparcamientoService.insertAparcamiento(aparcamiento)
                                .map(saved -> ResponseEntity.created(URI.create("/aparcamiento/" + saved.getId()))
                                                .body(saved))
                                .defaultIfEmpty(ResponseEntity.badRequest().build());

        }

        @Operation(summary = "R2 - Editar aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking updated"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Parking not found")
        })
        @PutMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Aparcamiento>> updateAparcamiento(
                        @PathVariable String id,
                        @RequestBody AparcamientoDTO dto) {

                // Validaciones básicas 
                if (id == null || id.trim().isEmpty()) {
                        return Mono.just(ResponseEntity.badRequest().build());
                }
                if (dto.getDirection() == null || dto.getDirection().trim().isEmpty()) {
                        return Mono.just(ResponseEntity.badRequest().build());
                }
                if (dto.getBikesCapacity() < 0) {
                        return Mono.just(ResponseEntity.badRequest().build());
                }

                return aparcamientoService.update(id, dto)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @Operation(summary = "R3 - Borrar aparcamiento por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Aparcamiento borrado"),
                        @ApiResponse(responseCode = "404", description = "Aparcamiento no encontrado")
        })
        @DeleteMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Void>> deleteAparcamiento(@PathVariable String id) {
                return aparcamientoService.findById(id)
                                .flatMap(existing -> aparcamientoService.deleteById(id)
                                                .thenReturn(ResponseEntity.noContent().<Void>build()))
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        // EVENTOS ENDPOINTS

        @Operation(summary = "Obtener eventos por aparcamiento")
        @GetMapping("/evento/aparcamiento/{id}")
        public Flux<Evento> getEventosByAparcamiento(@PathVariable String id) {
                return eventoService.getEventosByAparcamiento(id);
        }

        @Operation(summary = "BR5 - Estado actual de un aparcamiento")
        @GetMapping("/evento/status/{id}")
        public Mono<ResponseEntity<Evento>> getStatus(@PathVariable String id) {
                return eventoService.getStatus(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Obtener eventos entre dos fechas")
        @GetMapping("/evento/entre-fechas")
        public Flux<Evento> getEventosEntreFechas(
                        @RequestParam String aparcamientoId,
                        @RequestParam Instant inicio,
                        @RequestParam Instant fin) {
                return eventoService.getEventosEntreFechas(aparcamientoId, inicio, fin);
        }

        @Operation(summary = "BR6 - Cambios de estado de un aparcamiento entre dos fechas")
        @GetMapping("/evento/cambios-estado")
        public Flux<Evento> getCambiosEstado(
                        @RequestParam String aparcamientoId,
                        @RequestParam Instant inicio,
                        @RequestParam Instant fin) {
                return eventoService.getCambiosEstado(aparcamientoId, inicio, fin);
        }

        @Operation(summary = "Obtener aparcamientos con bicis disponibles")
        @GetMapping("/evento/disponibles")
        public Flux<Evento> getDisponibles() {
                return eventoService.getDisponibles();
        }

        @Operation(summary = "AR2 - Obtener el promedio de bicicletas por aparcamiento")
        @GetMapping("/evento/promedios")
        public Flux<AverageResult> getPromedioBicisPorAparcamiento() {
                return eventoService.getPromedioBicisPorAparcamiento();
        }

        @Operation(summary = "BR7 - 10 aparcamientos con más disponibilidad en un momento dado")
        @GetMapping("/evento/top10")
        public Flux<Evento> getTop10(@RequestParam Instant timestamp) {
                return eventoService.getTop10PorDisponibilidad(timestamp);
        }

        @Operation(summary = "AR1 - Obtener el aparcamiento con bicis disponibles más cercano a una posición dada")
        @GetMapping("/aparcamiento/disponible-cercano")
        public Mono<ResponseEntity<Aparcamiento>> getDisponibleCercano(
                        @RequestParam float latitud,
                        @RequestParam float longitud) {

        return eventoService.getDisponibleByLocation(latitud, longitud)
            .flatMap(evento -> {
                String aparcamientoId = evento.getAparcamientoId();
                return aparcamientoService.findById(aparcamientoId)
                        .map(ResponseEntity::ok);
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @Operation(summary = "BR8 - Registro de un evento")
        @PostMapping("/evento")
        public Mono<ResponseEntity<Evento>> save(@RequestBody Evento evento) {
                return eventoService.save(evento)
                                .map(saved -> ResponseEntity.ok(saved));
        }

        @Operation(summary = "Borrar un evento por ID")
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
