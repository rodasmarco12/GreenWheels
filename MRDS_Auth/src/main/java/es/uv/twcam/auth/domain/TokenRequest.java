package es.uv.twcam.auth.domain;
import java.util.List;

public class TokenRequest {
    private String name;
    private List<String> roles;

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
