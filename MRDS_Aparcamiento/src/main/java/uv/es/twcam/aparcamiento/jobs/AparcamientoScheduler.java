package uv.es.twcam.aparcamiento.jobs;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uv.es.twcam.aparcamiento.domain.Evento;

@Component
@SuppressWarnings("unused")
public class AparcamientoScheduler {

    @Value("${app.scheduling.enabled}")
    private String schedulerEnabled;

    private final Random random = new Random();

    private final WebClient webClient;

    public AparcamientoScheduler(
            WebClient.Builder webClientBuilder,
            @Value("${bicicletas.url}") String bicicletasUrl) {
        this.webClient = webClientBuilder.baseUrl(bicicletasUrl).build();
    }

    // Método para enviar eventos al microservicio
    private void sendEvent(Evento evento) {
        webClient.post()
                .uri("/evento")
                .bodyValue(evento)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    // Coordenadas fijas por parking_id
    private final Map<Integer, int[]> parkingCoordinates = Map.ofEntries(
            Map.entry(1, new int[] { 120, 880 }),
            Map.entry(2, new int[] { 230, 670 }),
            Map.entry(3, new int[] { 540, 520 }),
            Map.entry(4, new int[] { 760, 300 }),
            Map.entry(5, new int[] { 980, 450 }),
            Map.entry(6, new int[] { 310, 120 }),
            Map.entry(7, new int[] { 410, 780 }),
            Map.entry(8, new int[] { 620, 640 }),
            Map.entry(9, new int[] { 150, 220 }),
            Map.entry(10, new int[] { 860, 190 }),
            Map.entry(11, new int[] { 275, 900 }),
            Map.entry(12, new int[] { 470, 740 }),
            Map.entry(13, new int[] { 690, 110 }),
            Map.entry(14, new int[] { 840, 510 }),
            Map.entry(15, new int[] { 510, 630 }),
            Map.entry(16, new int[] { 660, 360 }),
            Map.entry(17, new int[] { 135, 470 }),
            Map.entry(18, new int[] { 935, 820 }),
            Map.entry(19, new int[] { 370, 260 }),
            Map.entry(20, new int[] { 725, 390 }));

    private final List<String> operations = List.of("aparcamiento", "alquiler", "reposición_múltiple",
            "retirada_múltiple");

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}")
    public void runPeriodicTask() {
        if (Boolean.parseBoolean(this.schedulerEnabled)) {
            for (int id = 1; id <= 20; id++) {
                String operation = operations.get(random.nextInt(operations.size()));
                int bikesAvailable = random.nextInt(51); // 0 a 50
                int freeParkingSpots = 50 - bikesAvailable;
                Instant timestamp = Instant.now();
                int[] coords = parkingCoordinates.get(id);
                int latitude = coords[0];
                int longitude = coords[1];

                // Crear el evento
                Evento evento = new Evento(
                        UUID.randomUUID().toString(),
                        String.valueOf(id),
                        operation,
                        bikesAvailable,
                        freeParkingSpots,
                        latitude,
                        longitude,
                        timestamp);

                // Enviar el evento al microservicio
                sendEvent(evento);

                System.out.println("Evento enviado: " + evento.toString());

            }
        }

    }
}