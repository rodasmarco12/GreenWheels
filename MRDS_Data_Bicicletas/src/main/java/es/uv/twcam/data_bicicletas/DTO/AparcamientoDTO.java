package es.uv.twcam.data_bicicletas.DTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AparcamientoDTO {

    @NotBlank(message = "La dirección es obligatoria")
    private String direction;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 0, message = "La capacidad no puede ser negativa")
    private int bikesCapacity;

    @NotNull(message = "La latitud es obligatoria")
    private float latitude;

    @NotNull(message = "La longitud es obligatoria")
    private float longitude;

    public AparcamientoDTO() { 
    }

    public AparcamientoDTO(String direction, int bikesCapacity, float latitude, float longitude) {
        this.direction = direction;
        this.bikesCapacity = bikesCapacity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters y setters...

    

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
