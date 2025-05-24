package es.uv.twcam.data_polucion.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

// Tabla "station" de MySQL
@Table(name = "station")
public class Station {

    @Id
    private String id;

    @Column("direction")
    private String direction;

    @Column("latitude")
    private float latitude;

    @Column("longitude")
    private float longitude;

    public Station() {
    }

    public Station(String direction, float latitude, float longitude) {
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
