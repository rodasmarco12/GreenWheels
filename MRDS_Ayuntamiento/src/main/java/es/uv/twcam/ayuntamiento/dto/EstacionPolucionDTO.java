package es.uv.twcam.ayuntamiento.dto;

public class EstacionPolucionDTO {
    private String idStation;
    private float latitud;
    private float longitud;
    private AveragePollutionDTO air_quality;

    // Getters y setters

    public EstacionPolucionDTO() {
    }

    public EstacionPolucionDTO(String idStation, float latitud, float longitud, AveragePollutionDTO air_quality) {
        this.idStation = idStation;
        this.latitud = latitud;
        this.longitud = longitud;
        this.air_quality = air_quality;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public AveragePollutionDTO getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(AveragePollutionDTO air_quality) {
        this.air_quality = air_quality;
    }
}
