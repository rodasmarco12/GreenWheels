package es.uv.twcam.data_ayun.domain;


import es.uv.twcam.data_ayun.dto.AveragePollutionDTO;


public class AggregatedData {
    
    
    private String aparcamientoId;
    private double average_bikesAvailable;
    private AveragePollutionDTO air_quality;

    public AggregatedData() {}

    public String getAparcamientoId() {
        return aparcamientoId;
    }

    public void setAparcamientoId(String aparcamientoId) {
        this.aparcamientoId = aparcamientoId;
    }

    public double getAverage_bikesAvailable() {
        return average_bikesAvailable;
    }

    public void setAverage_bikesAvailable(double average_bikesAvailable) {
        this.average_bikesAvailable = average_bikesAvailable;
    }

    public AveragePollutionDTO getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(AveragePollutionDTO air_quality) {
        this.air_quality = air_quality;
    }
}

