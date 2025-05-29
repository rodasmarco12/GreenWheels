package es.uv.twcam.polucion.DTO;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AveragePollutionResponseDTO {
    private Instant timeStamp;
    private List<AveragePollutionDTO> aggregatedData;
}
