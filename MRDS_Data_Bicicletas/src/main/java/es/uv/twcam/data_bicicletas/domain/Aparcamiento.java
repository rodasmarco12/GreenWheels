package es.uv.twcam.data_bicicletas.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table("aparcamiento")
public class Aparcamiento {

    @Id
    private String id; // Puedes generar UUID tú mismo

    @Column("direction")
    private String direction;

    @Column("bikesCapacity")
    private int bikesCapacity;

    @Column("latitude")
    private float latitude;

    @Column("longitude")
    private float longitude;

    public Aparcamiento() { 
    }

    public Aparcamiento(String direction, int bikesCapacity, float latitude, float longitude) {
        this.direction = direction;
        this.bikesCapacity = bikesCapacity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters y setters...

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

    public int getBikesCapacity() {
        return bikesCapacity;
    }

    public void setBikesCapacity(int bikesCapacity) {
        this.bikesCapacity = bikesCapacity;
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
