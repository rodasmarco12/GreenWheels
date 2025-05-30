package es.uv.twcam.polucion.controllers;

import es.uv.twcam.polucion.DTO.AirQualityDTO;
import es.uv.twcam.polucion.DTO.AveragePollutionDTO;
import es.uv.twcam.polucion.DTO.AveragePollutionResponseDTO;
import es.uv.twcam.polucion.DTO.ReadingDTO;
import es.uv.twcam.polucion.DTO.StationDTO;
import es.uv.twcam.polucion.security.RoleValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/polucion")
@RequiredArgsConstructor
public class PolucionController {

        private final WebClient webClient;
        private final RoleValidator roleValidator;

        @Operation(summary = "Crear una nueva estación de medición", security = @SecurityRequirement(name = "x-auth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Estación creada exitosamente.", content = @Content(schema = @Schema(implementation = StationDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
                        @ApiResponse(responseCode = "401", description = "No autorizado."),
                        @ApiResponse(responseCode = "409", description = "Conflicto - estación ya existente."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @PostMapping("/estacion")
        public Mono<ResponseEntity<StationDTO>> createStation(@RequestBody StationDTO dto) {
                return roleValidator.hasRole("ADMIN")
                                .flatMap(hasRole -> {
                                        if (!hasRole) {
                                                return Mono.just(ResponseEntity.status(403).build());
                                        }
                                        return webClient.post()
                                                        .uri("/estacion")
                                                        .bodyValue(dto)
                                                        .retrieve()
                                                        .toEntity(StationDTO.class);
                                });
        }

        @Operation(summary = "Actualizar información de una estación", security = @SecurityRequirement(name = "x-auth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estación actualizada correctamente.", content = @Content(schema = @Schema(implementation = StationDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
                        @ApiResponse(responseCode = "401", description = "No autorizado."),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @PutMapping("/estacion/update/{id}")
        public Mono<ResponseEntity<StationDTO>> updateStation(@PathVariable String id, @RequestBody StationDTO dto) {
                return roleValidator.hasRole("ADMIN")
                                .flatMap(hasRole -> {
                                        if (!hasRole) {
                                                return Mono.just(ResponseEntity.status(403).build());
                                        }
                                        return webClient.put()
                                                        .uri("/estacion/" + id)
                                                        .bodyValue(dto)
                                                        .retrieve()
                                                        .toEntity(StationDTO.class);
                                });
        }

        @Operation(summary = "Eliminar una estación", security = @SecurityRequirement(name = "x-auth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Estación eliminada correctamente."),
                        @ApiResponse(responseCode = "401", description = "No autorizado."),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @DeleteMapping("/estacion/{id}")
        public Mono<ResponseEntity<Void>> deleteStation(@PathVariable String id) {
                return roleValidator.hasRole("ADMIN")
                                .flatMap(hasRole -> {
                                        if (!hasRole) {
                                                return Mono.just(ResponseEntity.status(403).build());
                                        }
                                        return webClient.delete()
                                                        .uri("/estacion/" + id)
                                                        .retrieve()
                                                        .toBodilessEntity();
                                });
        }

        @Operation(summary = "Obtener todas las estaciones registradas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de estaciones.", content = @Content(schema = @Schema(implementation = StationDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estaciones")
        public Flux<StationDTO> getAllStations() {
                return webClient.get()
                                .uri("/estaciones")
                                .retrieve()
                                .bodyToFlux(StationDTO.class);
        }

        @Operation(summary = "Enviar una nueva lectura a una estación", security = @SecurityRequirement(name = "x-auth"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lectura registrada.", content = @Content(schema = @Schema(implementation = ReadingDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos."),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @PostMapping("/estacion/{id}")
        public Mono<ResponseEntity<ReadingDTO>> submitReading(@PathVariable String id, @RequestBody ReadingDTO dto) {
                return roleValidator.hasRole("ESTACION")
                                .flatMap(hasRole -> {
                                        if (!hasRole) {
                                                return Mono.just(ResponseEntity.status(403).build());
                                        }
                                        return webClient.post()
                                                        .uri("/estacion/" + id)
                                                        .bodyValue(dto)
                                                        .retrieve()
                                                        .toEntity(ReadingDTO.class);
                                });
        }

        @Operation(summary = "Obtener la última lectura de una estación")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Última lectura recuperada.", content = @Content(schema = @Schema(implementation = ReadingDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estacion/{id}/status")
        public Mono<ResponseEntity<ReadingDTO>> getLastReading(@PathVariable String id) {
                return webClient.get()
                                .uri("/estacion/" + id + "/status")
                                .retrieve()
                                .toEntity(ReadingDTO.class);
        }

        @Operation(summary = "Obtener lecturas en un intervalo de tiempo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lecturas obtenidas.", content = @Content(schema = @Schema(implementation = ReadingDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Parámetros inválidos."),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estacion/{id}/status-range")
        public Flux<ReadingDTO> getReadingsBetween(@PathVariable String id,
                        @RequestParam Instant from,
                        @RequestParam Instant to) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/estacion/" + id + "/lectura-status")
                                                .queryParam("from", from)
                                                .queryParam("to", to)
                                                .build())
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class);
        }

        @Operation(summary = "Obtener estadísticas de todas las estaciones")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lecturas obtenidas.", content = @Content(schema = @Schema(implementation = ReadingDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Parámetros inválidos."),
                        @ApiResponse(responseCode = "404", description = "Estación no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estacion/estadistica")
        public Flux<ReadingDTO> getStatistics() {
                return webClient.get()
                                .uri("/estacion/lecturas")
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class);
        }

        @Operation(summary = "Obtener promedio de contaminantes atmosféricos por estación")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Promedios por estación calculados correctamente.", content = @Content(schema = @Schema(implementation = AveragePollutionResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estadisticas/medias")
        public Mono<AveragePollutionResponseDTO> getAveragesByStation() {
                Mono<List<ReadingDTO>> lecturasMono = webClient.get()
                                .uri("/estacion/lecturas")
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class)
                                .collectList();

                Mono<List<StationDTO>> estacionesMono = webClient.get()
                                .uri("/estaciones")
                                .retrieve()
                                .bodyToFlux(StationDTO.class)
                                .collectList();

                return Mono.zip(lecturasMono, estacionesMono)
                                .map(tuple -> {
                                        List<ReadingDTO> lecturas = tuple.getT1();
                                        List<StationDTO> estaciones = tuple.getT2();

                                        Map<String, List<ReadingDTO>> lecturasPorEstacion = lecturas.stream()
                                                        .collect(Collectors.groupingBy(ReadingDTO::getStationId));

                                        Map<String, StationDTO> mapaEstaciones = estaciones.stream()
                                                        .collect(Collectors.toMap(StationDTO::getId, s -> s));

                                        List<AveragePollutionDTO> resultado = new ArrayList<>();

                                        lecturasPorEstacion.forEach((stationId, lista) -> {
                                                StationDTO estacion = mapaEstaciones.get(stationId);
                                                if (estacion != null) {
                                                        AveragePollutionDTO dto = new AveragePollutionDTO();
                                                        dto.setIdStation(stationId);
                                                        dto.setLatitud(estacion.getLatitude());
                                                        dto.setLongitud(estacion.getLongitude());

                                                        AirQualityDTO air = new AirQualityDTO();
                                                        air.setNitricOxides(lista.stream()
                                                                        .mapToDouble(ReadingDTO::getNitricOxides)
                                                                        .average().orElse(0.0));
                                                        air.setNitrogenDioxides(lista.stream()
                                                                        .mapToDouble(ReadingDTO::getNitrogenDioxides)
                                                                        .average().orElse(0.0));
                                                        air.setVOCs_NMHC(lista.stream()
                                                                        .mapToDouble(ReadingDTO::getVOCs_NMHC).average()
                                                                        .orElse(0.0));
                                                        air.setPM2_5(lista.stream().mapToDouble(ReadingDTO::getPM2_5)
                                                                        .average().orElse(0.0));

                                                        dto.setAir_quality(air);
                                                        resultado.add(dto);
                                                }
                                        });

                                        AveragePollutionResponseDTO response = new AveragePollutionResponseDTO();
                                        response.setTimeStamp(Instant.now());
                                        response.setAggregatedData(resultado);

                                        return response;
                                });
        }

}