package es.uv.twcam.ayuntamiento.dto;

public class StationDTO {

    private String id;
    private String direction;
    private float latitude;
    private float longitude;

    public StationDTO() {
    }

    public StationDTO(String direction, float latitude, float longitude) {
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

}
