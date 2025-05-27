package es.uv.twcam.bicicletas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import es.uv.twcam.bicicletas.domain.Aparcamiento;
import es.uv.twcam.bicicletas.domain.Evento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import es.uv.twcam.bicicletas.DTO.EventoDTO;
import es.uv.twcam.bicicletas.DTO.AverageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;

import es.uv.twcam.bicicletas.DTO.AparcamientoDTO;

@RestController
@RequestMapping("/api/v1/bicicletas")
@CrossOrigin
public class BicicletasController {

        private final WebClient webClient;

        public BicicletasController(WebClient.Builder webClientBuilder,
                        @Value("${data.bicicletas.url}") String dataUrl) {
                this.webClient = webClientBuilder
                                .baseUrl(dataUrl)
                                .build();
        }

        // R1: Se pueden añadir aparcamientos. Un aparcamiento tiene un ID, está en una
        // dirección, puede
        // alojar un número máximo de bicicletas, y tiene datos de geolocalización
        // (latitud y longitud).
        // Rol: admin.
        @Operation(summary = "BR1 - Añadir aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Parking created"),
                        @ApiResponse(responseCode = "400", description = "Invalid parking data")
        })
        @PostMapping("/aparcamiento")
        public Mono<ResponseEntity<Aparcamiento>> create(@RequestBody AparcamientoDTO aparcamiento) {
                return webClient.post()
                                .uri("/aparcamiento") // Ruta del endpoint original
                                .bodyValue(aparcamiento)
                                .retrieve()
                                .bodyToMono(Aparcamiento.class)
                                .map(ResponseEntity::ok);
        }

        // R2: Se puede editar un aparcamiento.
        // Rol: admin.
        @Operation(summary = "BR2 - Editar aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking updated"),
                        @ApiResponse(responseCode = "404", description = "Parking not found")
        })
        @PutMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Aparcamiento>> update(@PathVariable String id,
                        @RequestBody AparcamientoDTO aparcamiento) {
                return webClient.put()
                                .uri("/aparcamiento/" + id)
                                .bodyValue(aparcamiento)
                                .retrieve()
                                .bodyToMono(Aparcamiento.class)
                                .map(ResponseEntity::ok);
        }

        // R3: Se puede borrar un aparcamiento.
        // Rol: admin.
        @Operation(summary = "BR3 - Borrar aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Parking deleted"),
                        @ApiResponse(responseCode = "404", description = "Parking not found")
        })
        @DeleteMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
                return webClient.delete()
                                .uri("/aparcamiento/" + id)
                                .retrieve()
                                .toBodilessEntity()
                                .map(response -> ResponseEntity.status(response.getStatusCode()).<Void>build())
                                .onErrorResume(WebClientResponseException.NotFound.class,
                                                e -> Mono.just(ResponseEntity.notFound().build()));
        }

        // R4: Es posible obtener un listado de todos los aparcamientos
        @Operation(summary = "BR4 - Obtener todos los aparcamientos")
        @GetMapping("/aparcamientos")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parkings found"),
                        @ApiResponse(responseCode = "404", description = "No parkings found")
        })
        public ResponseEntity<Flux<Aparcamiento>> getAllAparcamientos() {
                Flux<Aparcamiento> aparcamientos = webClient.get()
                                .uri("/aparcamiento")
                                .retrieve()
                                .bodyToFlux(Aparcamiento.class);
                return ResponseEntity.ok(aparcamientos);
        }

        // BR5: Es posible consultar el estado (número de huecos y de bicis disponibles)
        // de un aparcamiento
        // dado.
        @Operation(summary = "BR5 - Consultar estado de un aparcamiento")
        @GetMapping("/aparcamiento/{id}/status")
        public Mono<ResponseEntity<Evento>> getEstadoAparcamiento(@PathVariable String id) {
                return webClient.get()
                                .uri("/evento/status/" + id)
                                .retrieve()
                                .bodyToMono(Evento.class)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        // BR6: Cambios en el aparcamiento en un intervalo de tiempo.
        @Operation(summary = "BR6 - Consultar cambios en un aparcamiento en un intervalo de tiempo")
        @GetMapping("/aparcamiento/{id}/status-changes")
        public Flux<Evento> getCambiosAparcamiento(@PathVariable String id,
                        @RequestParam Instant inicio,
                        @RequestParam Instant fin) {
                return webClient.get()
                                .uri("/aparcamiento/cambio-estado?aparcamientoId=" + id + "&inicio=" + inicio + "&fin="
                                                + fin)
                                .retrieve()
                                .bodyToFlux(Evento.class);
        }

        // BR7: Es posible consultar los 10 aparcamientos con mayor número de bicis
        // disponibles en un
        // momento determinado.
        @Operation(summary = "BR7 - Consultar los 10 aparcamientos con mayor número de bicis disponibles")
        @GetMapping("/aparcamiento/top")
        public Flux<Aparcamiento> getTopAparcamientos(@RequestParam Instant timestamp) {
                return webClient.get()
                                .uri("/evento/top10?timestamp=" + timestamp)
                                .retrieve()
                                .bodyToFlux(Aparcamiento.class);
        }

        // BR8: Cuando se aparque una bici o se alquile una bici, notificará el id, el
        // tipo de operación
        // realizada (aparcamiento, alquiler, reposición_múltiple, retirada_múltiple),
        // el número de
        // huecos, de bicis, y el instante en el que se ha producido este cambio.
        // Rol: aparcamiento
        @Operation(summary = "BR8 - Crear evento ")
        @PostMapping("/evento")
        public Mono<ResponseEntity<Evento>> createEvento(@RequestBody Evento evento) {
                return webClient.post()
                                .uri("/evento")
                                .bodyValue(evento)
                                .retrieve()
                                .bodyToMono(Evento.class)
                                .map(ResponseEntity::ok);
        }

}
