package es.uv.twcam.ayuntamiento.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;

import es.uv.twcam.ayuntamiento.domain.AggregatedData;
import es.uv.twcam.ayuntamiento.domain.Aparcamiento;
import es.uv.twcam.ayuntamiento.domain.StatisticsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;
import es.uv.twcam.ayuntamiento.dto.AparcamientoDTO;
import es.uv.twcam.ayuntamiento.dto.AveragePollutionDTO;
import es.uv.twcam.ayuntamiento.dto.AveragePollutionResponseDTO;
import es.uv.twcam.ayuntamiento.dto.BicicletaPromedioDTO;
import es.uv.twcam.ayuntamiento.dto.EstacionPolucionDTO;
import es.uv.twcam.ayuntamiento.dto.StationDTO;

@RestController
@RequestMapping("api/v1/ayuntamiento")
@CrossOrigin
public class AyuntamientoController {

        private final WebClient dataBicicletas;

        private final WebClient bicicletas;

        private final WebClient polucion;

        private final WebClient dataPolucion;

        private final WebClient dataAyuntamiento;

        @Value("${system.token}")
        private String systemToken;

        // Constructor que inicializa el WebClient con la URL base

        public AyuntamientoController(WebClient.Builder webClientBuilder,
                        @Value("${data.bicicletas.url}") String dataBicicletasUrl,
                        @Value("${bicicletas.url}") String bicicletasUrl,
                        @Value("${polucion.url}") String polucionUrl,
                        @Value("${data.polucion.url}") String dataPolucionUrl,
                        @Value("${data.ayuntamiento.url}") String dataAyuntamientoUrl) {
                this.dataBicicletas = webClientBuilder
                                .baseUrl(dataBicicletasUrl)
                                .build();
                this.bicicletas = webClientBuilder
                                .baseUrl(bicicletasUrl)
                                .build();
                this.polucion = webClientBuilder
                                .baseUrl(polucionUrl)
                                .build();
                this.dataPolucion = webClientBuilder
                                .baseUrl(dataPolucionUrl)
                                .build();
                this.dataAyuntamiento = webClientBuilder
                                .baseUrl(dataAyuntamientoUrl)
                                .build();

        }

        // AR1: Obtener el aparcamiento con bicis disponibles
        // más cercano a una posición dada.
        @Operation(summary = "AR1 - Obtener aparcamiento más cercano con bicis disponibles")
        @GetMapping("/disponible-cercano")
        public Mono<ResponseEntity<Aparcamiento>> getDisponible(@RequestParam float latitud,
                        @RequestParam float longitud) {
                return dataBicicletas.get()
                                .uri("/aparcamiento/disponible-cercano?latitud=" + latitud + "&longitud=" + longitud)
                                .retrieve()
                                .bodyToMono(Aparcamiento.class)
                                .map(ResponseEntity::ok);
        }

        // AR2: Agregar Estadisticas
        @GetMapping("/aggregateData")
        public Mono<ResponseEntity<Void>> getEstadisticas() {
                Mono<List<BicicletaPromedioDTO>> bicicletasMono = dataBicicletas.get()
                                .uri("/evento/promedios")
                                .retrieve()
                                .bodyToFlux(BicicletaPromedioDTO.class)
                                .collectList();

                Mono<AveragePollutionResponseDTO> polucionMono = polucion.get()
                                .uri("/estadisticas/medias")
                                .retrieve()
                                .bodyToMono(AveragePollutionResponseDTO.class);

                return Mono.zip(bicicletasMono, polucionMono)
                                .flatMap(tuple -> {
                                        List<BicicletaPromedioDTO> bicicletas = tuple.getT1();
                                        List<EstacionPolucionDTO> estaciones = tuple.getT2().getAggregatedData();

                                        List<AggregatedData> datos = bicicletas.stream().map(bici -> {
                                                EstacionPolucionDTO estacionCercana = estaciones.stream()
                                                                .min(Comparator.comparingDouble(estacion -> distancia(
                                                                                bici.getLatitude(), bici.getLongitude(),
                                                                                estacion.getLatitud(),
                                                                                estacion.getLongitud())))
                                                                .orElse(null);

                                                AggregatedData agg = new AggregatedData();
                                                agg.setAparcamientoId(bici.getId());
                                                agg.setAverage_bikesAvailable(bici.getAvgBikesAvailable());
                                                agg.setAir_quality(estacionCercana != null
                                                                ? estacionCercana.getAir_quality()
                                                                : new AveragePollutionDTO());
                                                return agg;
                                        }).toList();

                                        StatisticsData finalData = new StatisticsData();
                                        finalData.setId("1");
                                        finalData.setTimeStamp(Instant.now());
                                        finalData.setAggregatedData(datos);

                                        return dataAyuntamiento.post()
                                                        .uri("/statistics")
                                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                                        .bodyValue(finalData)
                                                        .retrieve()
                                                        .toBodilessEntity()
                                                        .map(response -> ResponseEntity.ok().build());

                                });
        }

        // private double distancia(double lat1, double lon1, double lat2, double lon2)
        // {
        // double dLat = Math.toRadians(lat2 - lat1);
        // double dLon = Math.toRadians(lon2 - lon1);
        // double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        // + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        // * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        // return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // distancia en
        // km
        // }
        private double distancia(double lat1, double lon1, double lat2, double lon2) {
                double dLat = lat2 - lat1;
                double dLon = lon2 - lon1;
                return Math.sqrt(dLat * dLat + dLon * dLon);
        }

        // AR3: Obtener los ultimos agregados
        @Operation(summary = "AR3 - Obtener los últimos datos añadidos")
        @GetMapping("/aggregatedData")
        public Mono<ResponseEntity<StatisticsData>> getUltimos() {
                return dataAyuntamiento.get()
                                .uri("/statistics/latest")
                                .retrieve()
                                .bodyToMono(StatisticsData.class)
                                // .bodyToMono(String.class)
                                // .doOnNext(body -> System.out.println("Respuesta cruda: " + body))
                                .map(ResponseEntity::ok);
        }

        // AR4: Acceso a metodos Admin

        // Bicicletas

        // BR1: Se pueden añadir aparcamientos. Un aparcamiento tiene un ID, está en una
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
        public Mono<ResponseEntity<Aparcamiento>> createAparcamiento(@RequestBody AparcamientoDTO aparcamiento) {
                return bicicletas.post()
                                .uri("/aparcamiento")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .bodyValue(aparcamiento)
                                .retrieve()
                                .bodyToMono(Aparcamiento.class)
                                .map(ResponseEntity::ok);
        }

        // BR2: Se puede editar un aparcamiento.
        // Rol: admin.
        @Operation(summary = "BR2 - Editar aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking updated"),
                        @ApiResponse(responseCode = "404", description = "Parking not found")
        })
        @PutMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Aparcamiento>> update(@PathVariable String id,
                        @RequestBody AparcamientoDTO aparcamiento) {
                return bicicletas.put()
                                .uri("/aparcamiento/" + id)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .bodyValue(aparcamiento)
                                .retrieve()
                                .bodyToMono(Aparcamiento.class)
                                .map(ResponseEntity::ok);
        }

        // BR3: Se puede borrar un aparcamiento.
        // Rol: admin.
        @Operation(summary = "BR3 - Borrar aparcamiento")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Parking deleted"),
                        @ApiResponse(responseCode = "404", description = "Parking not found")
        })
        @DeleteMapping("/aparcamiento/{id}")
        public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
                return bicicletas.delete()
                                .uri("/aparcamiento/" + id)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .retrieve()
                                .toBodilessEntity()
                                .map(response -> ResponseEntity.status(response.getStatusCode()).<Void>build())
                                .onErrorResume(WebClientResponseException.class, e -> {
                                        if (e.getStatusCode().value() == 404) {
                                                return Mono.just(ResponseEntity.notFound().build());
                                        } else {
                                                return Mono.just(
                                                                ResponseEntity.status(e.getStatusCode()).<Void>build());
                                        }
                                });
        }

        // Polucion
        // PR1: Se pueden a~nadir estaciones.
        // Rol: admin

        @Operation(summary = "PR1 - Añadir estación")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Station created"),
                        @ApiResponse(responseCode = "400", description = "Invalid station data")
        })
        @PostMapping("/estacion")
        public Mono<ResponseEntity<StationDTO>> createEstacion(@RequestBody StationDTO stationDTO) {
                return polucion.post()
                                .uri("/estacion")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .bodyValue(stationDTO)
                                .retrieve()
                                .bodyToMono(StationDTO.class)
                                .map(ResponseEntity::ok);
        }

        // PR2: Se puede editar una estación.
        // Rol: admin
        @Operation(summary = "PR2 - Editar estación")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "station updated"),
                        @ApiResponse(responseCode = "404", description = "station not found")
        })
        @PutMapping("/estacion/{id}")
        public Mono<ResponseEntity<StationDTO>> update(@PathVariable String id,
                        @RequestBody StationDTO stationDTO) {
                return bicicletas.put()
                                .uri("/estacion/" + id)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .bodyValue(stationDTO)
                                .retrieve()
                                .bodyToMono(StationDTO.class)
                                .map(ResponseEntity::ok);
        }

        // PR3: Se puede borrar una estación.
        // Rol: admin
        @Operation(summary = "PR3 - Borrar estación")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Station deleted"),
                        @ApiResponse(responseCode = "404", description = "Station not found")
        })
        @DeleteMapping("/estacion/{id}")
        public Mono<ResponseEntity<Void>> delete(@PathVariable String id,
                        @RequestParam(required = false) String systemToken) {
                return polucion.delete()
                                .uri("/estacion/" + id)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                                .retrieve()
                                .toBodilessEntity()
                                .map(response -> ResponseEntity.status(response.getStatusCode()).<Void>build())
                                .onErrorResume(WebClientResponseException.class, e -> {
                                        if (e.getStatusCode().value() == 404) {
                                                return Mono.just(ResponseEntity.notFound().build());
                                        } else {
                                                return Mono.just(
                                                                ResponseEntity.status(e.getStatusCode()).<Void>build());
                                        }
                                });
        }
}
