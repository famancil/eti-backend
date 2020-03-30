package cl.cbb.gdeh.message.response;

import java.util.Collection;
import java.util.List;

import cl.cbb.gdeh.entities.Rol;
import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String name;
    private List<Rol> roles;

    public JwtResponse(String accessToken, String email,String name, List<Rol> roles) {
        this.token = accessToken;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
