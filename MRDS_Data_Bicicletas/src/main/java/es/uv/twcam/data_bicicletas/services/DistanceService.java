package es.uv.twcam.data_bicicletas.services;

import org.springframework.stereotype.Service;

import es.uv.twcam.data_bicicletas.domain.Evento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DistanceService {

       public  Mono<Evento> findClosestEvent(Flux<Evento> events, float targetLat, float targetLon) {
        return events
            .map(event -> new DistanceEventoPair(event, calculateEuclideanDistance(
                targetLat, targetLon, event.getLatitude(), event.getLongitude())))
            .sort((a, b) -> Float.compare(a.distance, b.distance))
            .next()
            .map(pair -> pair.event);
    }

    private static float calculateEuclideanDistance(float lat1, float lon1, float lat2, float lon2) {
        float dLat = lat2 - lat1;
        float dLon = lon2 - lon1;
        return (float) Math.sqrt(dLat * dLat + dLon * dLon);
    }

    private static class DistanceEventoPair {
        Evento event;
        float distance;

        DistanceEventoPair(Evento event, float distance) {
            this.event = event;
            this.distance = distance;
        }
    }

    
}
