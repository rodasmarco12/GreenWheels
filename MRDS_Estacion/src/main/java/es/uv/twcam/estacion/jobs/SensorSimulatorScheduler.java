package es.uv.twcam.estacion.jobs;

import es.uv.twcam.estacion.domain.ReadingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;
import java.util.Random;

@Component
public class SensorSimulatorScheduler {

    @Value("${app.scheduling.enabled}")
    private String schedulerEnabled;

    
        @Value("${system.token}")
        private String systemToken;

    private final WebClient webClient;
    private final Random random = new Random();

    private final Map<String, float[]> stationCoordinates = Map.ofEntries(
            Map.entry("1", new float[] { 39.4781f, -0.3567f }),
            Map.entry("2", new float[] { 39.4699f, -0.3763f }),
            Map.entry("3", new float[] { 39.4702f, -0.3580f }),
            Map.entry("4", new float[] { 39.4666f, -0.3579f }),
            Map.entry("5", new float[] { 39.4740f, -0.3684f }),
            Map.entry("6", new float[] { 39.4771f, -0.3520f }),
            Map.entry("7", new float[] { 39.4718f, -0.3702f }),
            Map.entry("8", new float[] { 39.4733f, -0.3595f }),
            Map.entry("9", new float[] { 39.4756f, -0.3651f }),
            Map.entry("10", new float[] { 39.4687f, -0.3542f }));

    public SensorSimulatorScheduler(WebClient.Builder webClientBuilder,
            @Value("${polucion.api.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}")
    public void sendSensorReadings() {
        if (!Boolean.parseBoolean(schedulerEnabled))
            return;

        stationCoordinates.forEach((id, coords) -> {
            ReadingRequest lectura = new ReadingRequest();
            lectura.setStationId(id);
            lectura.setTimeStamp(Instant.now());
            lectura.setNitricOxides(randomFloat(5, 50));
            lectura.setNitrogenDioxides(randomFloat(10, 60));
            lectura.setVOCs_NMHC(randomFloat(1, 20));
            lectura.setPM2_5(randomFloat(15, 80));
            lectura.setLatitude(coords[0]);
            lectura.setLongitude(coords[1]);

            webClient.post()
                    .uri("/lectura/" + id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                    .bodyValue(lectura)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnError(error -> System.err
                            .println("Error al enviar lectura a estación " + id + ": " + error.getMessage()))
                    .subscribe();

            System.out.println("Lectura enviada a estación " + id + ": " + lectura);
        });
    }

    private float randomFloat(int min, int max) {
        return min + random.nextFloat() * (max - min);
    }
}
