package es.uv.twcam.polucion.DTO;

import lombok.Data;

@Data
public class AirQualityDTO {
    private double nitricOxides;
    private double nitrogenDioxides;
    private double VOCs_NMHC;
    private double PM2_5;
}
