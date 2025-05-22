package es.uv.twcam.polucion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Entidad para la tabla "station" de MySQL
@Entity
@Table(name = "station")
public class Station {

    // Se genera un UUID para el id de la base de datos
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String id;

    @Column(name = "direction")
    private String direction;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

    public Station() {
    }

    // Getters y Setters

    public Station(String direccion, float latitude, float longitude) {
        this.direction = direccion;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direction;
    }

    public void setDireccion(String direccion) {
        this.direction = direccion;
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
