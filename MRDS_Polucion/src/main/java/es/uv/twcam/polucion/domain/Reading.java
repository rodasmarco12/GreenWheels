package es.uv.twcam.polucion.domain;

import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Document(collection = "pollution_readings")
public class Reading {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String stationId;
    private Instant timeStamp;
    private float nitricOxides;
    private float nitrogenDioxides;
    private float VOCs_NMHC;
    private float PM2_5;
    private float longitude;
    private float latitude;

    public Reading() {
    }

    public Reading(String stationId, Instant timeStamp, float nitricOxides, float nitrogenDioxides,
            float VOCs_NMHC, float PM2_5, float longitude, float latitude) {
        this.stationId = stationId;
        this.timeStamp = timeStamp;
        this.nitricOxides = nitricOxides;
        this.nitrogenDioxides = nitrogenDioxides;
        this.VOCs_NMHC = VOCs_NMHC;
        this.PM2_5 = PM2_5;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public float getlongitude() {
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

}
