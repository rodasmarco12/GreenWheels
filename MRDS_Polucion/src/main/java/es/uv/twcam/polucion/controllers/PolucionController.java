package es.uv.twcam.polucion.controllers;

import es.uv.twcam.polucion.DTO.AveragePollutionDTO;
import es.uv.twcam.polucion.DTO.ReadingDTO;
import es.uv.twcam.polucion.DTO.StationDTO;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/api/v1/polucion")
@RequiredArgsConstructor
public class PolucionController {

        private final WebClient webClient;

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
                return webClient.post()
                                .uri("/estacion")
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(StationDTO.class);
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
                return webClient.put()
                                .uri("/estacion/" + id)
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(StationDTO.class);
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
                return webClient.delete()
                                .uri("/estacion/" + id)
                                .retrieve()
                                .toBodilessEntity();
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
                return webClient.post()
                                .uri("/estacion/" + id)
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(ReadingDTO.class);
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
                        @ApiResponse(responseCode = "200", description = "Estadísticas generadas.", content = @Content(schema = @Schema(implementation = StationDTO.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estacion/estadistica")
        public Flux<ReadingDTO> getStatistics() {
                return webClient.get()
                                .uri("/estacion/lecturas")
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class);
        }

        @Operation(summary = "Obtener promedio de contaminantes atmosféricos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Promedios calculados correctamente.", content = @Content(schema = @Schema(implementation = AveragePollutionDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/estadisticas/medias")
        public Mono<AveragePollutionDTO> getAverages() {
                return webClient.get()
                                .uri("/estacion/lecturas")
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class)
                                .collectList()
                                .map(readings -> {
                                        AveragePollutionDTO dto = new AveragePollutionDTO();
                                        int total = readings.size();

                                        dto.setNitricOxidesAvg(
                                                        readings.stream().mapToDouble(ReadingDTO::getNitricOxides)
                                                                        .average().orElse(0.0));
                                        dto.setNitrogenDioxidesAvg(
                                                        readings.stream().mapToDouble(ReadingDTO::getNitrogenDioxides)
                                                                        .average().orElse(0.0));
                                        dto.setVocsNmhcAvg(
                                                        readings.stream().mapToDouble(ReadingDTO::getVOCs_NMHC)
                                                                        .average().orElse(0.0));
                                        dto.setPm25Avg(
                                                        readings.stream().mapToDouble(ReadingDTO::getPM2_5).average()
                                                                        .orElse(0.0));

                                        return dto;
                                });
        }

}