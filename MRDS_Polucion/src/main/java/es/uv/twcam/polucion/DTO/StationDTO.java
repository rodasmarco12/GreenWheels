package es.uv.twcam.polucion.DTO;

import lombok.Data;

@Data
public class StationDTO {
    private String id;
    private String direction;
    private float latitude;
    private float longitude;
}
