package es.uv.twcam.servicio.jobs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AyuntamientoScheduler {

    private final WebClient webClient;

    @Value("${ayuntamiento.api.url}")
    private String url;

    @Value("${system.token}")
    private String systemToken;

    public AyuntamientoScheduler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}")
    public void ejecutarGetEstadisticas() {
        System.out.println("Invocando GET a: " + url);

        webClient.get()
                .uri(url + "/aggregateData")
                .header("Authorization", "Bearer " + systemToken)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    System.out.println("GET exitoso: status " + response.getStatusCode());
                })
                .doOnError(error -> {
                    System.err.println("Error al invocar el endpoint: " + error.getMessage());
                })
                .subscribe();
    }
}
