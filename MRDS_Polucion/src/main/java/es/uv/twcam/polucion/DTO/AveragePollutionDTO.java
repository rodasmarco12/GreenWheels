package es.uv.twcam.polucion.DTO;

import lombok.Data;

@Data
public class AveragePollutionDTO {
    private double nitricOxidesAvg;
    private double nitrogenDioxidesAvg;
    private double vocsNmhcAvg;
    private double pm25Avg;
}
