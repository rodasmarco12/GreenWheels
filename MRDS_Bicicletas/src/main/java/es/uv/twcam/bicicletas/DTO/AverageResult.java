package es.uv.twcam.bicicletas.DTO;

public class AverageResult {
    private String id;
    private double avgBikesAvailable;
    private float latitude;
    private float longitude;

    // getters y setters
    public AverageResult() {
    }
    public AverageResult(String id, double avgBikesAvailable, float latitude, float longitude) {
        this.id = id;
        this.avgBikesAvailable = avgBikesAvailable;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAvgBikesAvailable() {
        return avgBikesAvailable;
    }

    public void setAvgBikesAvailable(double avgBikesAvailable) {
        this.avgBikesAvailable = avgBikesAvailable;
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
