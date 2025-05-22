package es.uv.twcam.data_bicicletas.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.twcam.data_bicicletas.DTO.AverageResult;
import es.uv.twcam.data_bicicletas.domain.Evento;
import es.uv.twcam.data_bicicletas.repositories.EventoRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class EventoService {
    // Inyección de dependencias
    // Repositorio de eventos
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private DistanceService distanceService;

    

    // Obtener todos los eventos por aparcamiento
    public Flux<Evento> getEventosByAparcamiento(String aparcamientoId) {
        return eventoRepository.findByAparcamientoId(aparcamientoId);
    }

    // Obtener el último evento por aparcamiento
    // Requerimiento: Es posible consultar el estado (número de huecos y de bicis disponibles) de un aparcamientodado.
    public Mono<Evento> getStatus(String aparcamientoId) {
        return eventoRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
    }

    // Obtener eventos entre dos fechas
    // Requerimiento: Es posible consultar todos los cambios de estado de un aparcamiento en un intervalo de tiempo.
    public Flux<Evento> getEventosEntreFechas(String aparcamientoId, Instant inicio, Instant fin) {
        return eventoRepository.findByAparcamientoIdAndTimestampBetween(aparcamientoId, inicio, fin);
    }

    // Obtener estado de cambios entre fechas 
    public Flux<Evento> getCambiosEstado(String aparcamientoId, Instant inicio, Instant fin) {
        return eventoRepository.findEstadoChangesByAparcamientoIdAndTimestampBetween(aparcamientoId, inicio, fin);
    }


    // Requerimiento: Obtener el aparcamiento con bicis disponibles más cercano a una posición dada.
    public Mono<Evento> getDisponibleByLocation(float latitud, float longitud) {
        return distanceService.findClosestEvent(eventoRepository.findDisponibles(), latitud, longitud);
    }


    // Obtener aparcamientos con bicis disponibles (último estado con bicis > 0)
    public Flux<Evento> getDisponibles() {
        return eventoRepository.findDisponibles();
    }

    // Promedio de bicis disponibles por aparcamiento
    // Requerimiento: Es posible consultar el promedio de bicis disponibles por aparcamiento.
    public Flux<AverageResult> getPromedioBicisPorAparcamiento() {
        return eventoRepository.findAverageBikesAvailableByAparcamientoId();
    }

    // Top 10 aparcamientos con más bicis en un momento específico
    // Requerimiento: Es posible consultar los 10 aparcamientos con más bicis disponibles en un momento dado.
    public Flux<Evento> getTop10PorDisponibilidad(Instant timestamp) {
        return eventoRepository.findTop10ByBikesAvailableAtTimestamp(timestamp);
    }

    // Guardar un nuevo evento
    // Requerimiento: Cuando se aparque una bici o se alquile una bici, notificará el id, el tipo de operación
    // realizada (aparcamiento, alquiler, reposición_múltiple, retirada_múltiple), el número de
    // huecos, de bicis, y el instante en el que se ha producido este cambio.
    public Mono<Evento> save(Evento evento) {
        return eventoRepository.save(evento);
    }

    // Eliminar evento por ID
    public Mono<Void> deleteById(String id) {
        return eventoRepository.deleteById(id);
    }

    
}
