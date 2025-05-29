package es.uv.twcam.polucion.DTO;

import lombok.Data;

@Data
public class AveragePollutionDTO {
    private String idStation;
    private double latitud;
    private double longitud;
    private AirQualityDTO air_quality;
}
