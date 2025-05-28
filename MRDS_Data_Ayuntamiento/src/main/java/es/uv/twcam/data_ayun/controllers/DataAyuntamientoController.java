package es.uv.twcam.data_ayun.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.uv.twcam.data_ayun.services.StatisticsService;
import es.uv.twcam.data_ayun.domain.StatisticsData;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;




@RestController
@RequestMapping("api/v1/data/ayuntamiento")
@CrossOrigin
public class DataAyuntamientoController {

    @Autowired
    private StatisticsService ss;

    @Operation(
        summary = "Obtener estadísticas entre dos fechas",
        description = "Devuelve los datos estadísticos entre las fechas de inicio y fin especificadas.",
        tags = {"Estadísticas"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas encontradas"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/statistics")
    public Flux<StatisticsData> getStatisticsBetween(
        @Parameter(description = "Fecha y hora de inicio (formato ISO-8601)", required = true)
        @RequestParam("start") Instant start,
        @Parameter(description = "Fecha y hora de fin (formato ISO-8601)", required = true)
        @RequestParam("end") Instant end) {
        return ss.getStatisticsBetween(start, end);
    }

    @Operation(
        summary = "AR3 - Obtener la estadística más reciente",
        description = "Devuelve el dato estadístico más reciente disponible.",
        tags = {"Estadísticas"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadística encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró estadística"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/statistics/latest")
    public Mono<StatisticsData> getLatestStatistics() {
        return ss.getLatestStatistics();
    }

    @Operation(
        summary = "AR2 - Guardar datos estadísticos",
        description = "Guarda los datos estadísticos proporcionados.",
        tags = {"Estadísticas"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadística guardada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/statistics")
    public Mono<ResponseEntity<StatisticsData>> save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos estadísticos a guardar",
            required = true
        )
        @RequestBody StatisticsData statisticsData) {
        return ss.save(statisticsData)
                .map(saved -> ResponseEntity.ok(saved));
    }

    @Operation(
        summary = "Obtener todas las estadísticas",
        description = "Devuelve todos los datos estadísticos almacenados.",
        tags = {"Estadísticas"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/statistics/all")
    public Flux<StatisticsData> getAllStatistics() {
        return ss.getAllStatistics();
    }

}
