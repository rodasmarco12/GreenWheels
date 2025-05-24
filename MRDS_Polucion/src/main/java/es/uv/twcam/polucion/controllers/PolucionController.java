package es.uv.twcam.polucion.controllers;

import es.uv.twcam.polucion.DTO.ReadingDTO;
import es.uv.twcam.polucion.DTO.StationDTO;
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

        @PostMapping("/estacion")
        public Mono<ResponseEntity<StationDTO>> createStation(@RequestBody StationDTO dto) {
                return webClient.post()
                                .uri("/estacion")
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(StationDTO.class);
        }

        @PutMapping("/estacion/update/{id}")
        public Mono<ResponseEntity<StationDTO>> updateStation(@PathVariable String id, @RequestBody StationDTO dto) {
                return webClient.put()
                                .uri("/estacion/" + id)
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(StationDTO.class);
        }

        @DeleteMapping("/estacion/{id}")
        public Mono<ResponseEntity<Void>> deleteStation(@PathVariable String id) {
                return webClient.delete()
                                .uri("/estacion/" + id)
                                .retrieve()
                                .toBodilessEntity();
        }

        @GetMapping("/estaciones")
        public Flux<StationDTO> getAllStations() {
                return webClient.get()
                                .uri("/estaciones")
                                .retrieve()
                                .bodyToFlux(StationDTO.class);
        }

        @PostMapping("/estacion/{id}")
        public Mono<ResponseEntity<ReadingDTO>> submitReading(@PathVariable String id, @RequestBody ReadingDTO dto) {
                return webClient.post()
                                .uri("/estacion/" + id + "/lectura")
                                .bodyValue(dto)
                                .retrieve()
                                .toEntity(ReadingDTO.class);
        }

        @GetMapping("/estacion/{id}/status")
        public Mono<ResponseEntity<ReadingDTO>> getLastReading(@PathVariable String id) {
                return webClient.get()
                                .uri("/estacion/" + id + "/ultima")
                                .retrieve()
                                .toEntity(ReadingDTO.class);
        }

        @GetMapping("/estacion/{id}/status-range")
        public Flux<ReadingDTO> getReadingsBetween(@PathVariable String id,
                        @RequestParam Instant from,
                        @RequestParam Instant to) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/estacion/" + id + "/intervalo")
                                                .queryParam("from", from)
                                                .queryParam("to", to)
                                                .build())
                                .retrieve()
                                .bodyToFlux(ReadingDTO.class);
        }

        @GetMapping("/estacion/estadistica")
        public Flux<StationDTO> getStatistics() {
                return webClient.get()
                                .uri("/estacion/estadistica")
                                .retrieve()
                                .bodyToFlux(StationDTO.class);
        }
}