package es.uv.twcam.ayuntamiento.domain;

public class Aparcamiento {


    private String id; // Puedes generar UUID tú mismo

    private String direction;

    private int bikesCapacity;


    private float latitude;
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
