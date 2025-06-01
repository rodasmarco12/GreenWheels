package es.uv.twcam.ayuntamiento.dto;

import java.util.List;

public class AveragePollutionResponseDTO {
    private List<EstacionPolucionDTO> aggregatedData;
    private String timeStamp;

    // Getters y setters

    public AveragePollutionResponseDTO() {
    }

    public AveragePollutionResponseDTO(List<EstacionPolucionDTO> aggregatedData, String timeStamp) {
        this.aggregatedData = aggregatedData;
        this.timeStamp = timeStamp;
    }

    public List<EstacionPolucionDTO> getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(List<EstacionPolucionDTO> aggregatedData) {
        this.aggregatedData = aggregatedData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
