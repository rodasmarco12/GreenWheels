package es.uv.twcam.estacion.domain;

import java.time.Instant;

public class ReadingRequest {
    private String stationId;
    private Instant timeStamp;
    private float nitricOxides;
    private float nitrogenDioxides;
    private float VOCs_NMHC;
    private float PM2_5;
    private float longitude;
    private float latitude;

    // Getters y Setters

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getNitricOxides() {
        return nitricOxides;
    }

    public void setNitricOxides(float nitricOxides) {
        this.nitricOxides = nitricOxides;
    }

    public float getNitrogenDioxides() {
        return nitrogenDioxides;
    }

    public void setNitrogenDioxides(float nitrogenDioxides) {
        this.nitrogenDioxides = nitrogenDioxides;
    }

    public float getVOCs_NMHC() {
        return VOCs_NMHC;
    }

    public void setVOCs_NMHC(float VOCs_NMHC) {
        this.VOCs_NMHC = VOCs_NMHC;
    }

    public float getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(float PM2_5) {
        this.PM2_5 = PM2_5;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ReadingRequest{" +
                "stationId='" + stationId + '\'' +
                ", timeStamp=" + timeStamp +
                ", nitricOxides=" + nitricOxides +
                ", nitrogenDioxides=" + nitrogenDioxides +
                ", VOCs_NMHC=" + VOCs_NMHC +
                ", PM2_5=" + PM2_5 +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
