package es.uv.twcam.ayuntamiento.dto;

public class AveragePollutionDTO {
        private double nitricOxides;
    private double nitrogenDioxides;
    private double VOCs_NMHC;
    private double PM2_5;

    public AveragePollutionDTO() {}

    public double getNitricOxides() {
        return nitricOxides;
    }

    public void setNitricOxides(double nitricOxides) {
        this.nitricOxides = nitricOxides;
    }

    public double getNitrogenDioxides() {
        return nitrogenDioxides;
    }

    public void setNitrogenDioxides(double nitrogenDioxides) {
        this.nitrogenDioxides = nitrogenDioxides;
    }

    public double getVOCs_NMHC() {
        return VOCs_NMHC;
    }

    public void setVOCs_NMHC(double VOCs_NMHC) {
        this.VOCs_NMHC = VOCs_NMHC;
    }

    public double getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(double PM2_5) {
        this.PM2_5 = PM2_5;
    }

    
}
