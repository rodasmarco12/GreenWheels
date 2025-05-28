package es.uv.twcam.bicicletas.DTO;

import java.time.Instant;

public class EventoDTO {
    
    private String aparcamientoId;

    private int bikesAvailable;
    private int freeParkingSpots;
    private float latitude;
    private float longitude;
    private Instant timestamp;

    public EventoDTO() {
    }

    public EventoDTO(String aparcamientoId, int bikesAvailable, int freeParkingSpots, float latitude, float longitude, Instant timestamp) {
        this.aparcamientoId = aparcamientoId;
        this.bikesAvailable = bikesAvailable;
        this.freeParkingSpots = freeParkingSpots;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getAparcamientoId() {
        return aparcamientoId;
    }

    public void setAparcamientoId(String aparcamientoId) {
        this.aparcamientoId = aparcamientoId;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public void setBikesAvailable(int bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public int getFreeParkingSpots() {
        return freeParkingSpots;
    }

    public void setFreeParkingSpots(int freeParkingSpots) {
        this.freeParkingSpots = freeParkingSpots;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
   
    
}
    