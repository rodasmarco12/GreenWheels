package es.uv.twcam.polucion.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class ReadingDTO {
    private String id;
    private String stationId;
    private Instant timeStamp;
    private float nitricOxides;
    private float nitrogenDioxides;
    private float VOCs_NMHC;
    private float PM2_5;
    private float longitude;
    private float latitude;
}