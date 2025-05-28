package es.uv.twcam.auth.security;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtService {

	@Value("${sys.token.key}")
	private String key;
	
	@Value("${sys.token.issuer}")
	private String issuer;
	
	// @Value("${sys.token.duration}")
	// private Integer duration;
	
	private Algorithm algorithm;
	private JWTVerifier verifier;
	
	@PostConstruct
	public void init(){
		this.algorithm = Algorithm.HMAC256(this.key.getBytes());
		this.verifier = JWT.require(this.algorithm).build();
	}
	
	public String generateAccessToken(String username, List<String> claims) {
		return JWT.create()
				 .withSubject(username)
				//  .withExpiresAt(new Date(System.currentTimeMillis()+this.duration))
				 .withIssuer(this.issuer)
				 .withClaim("roles", claims)
				 .sign(this.algorithm);
	}
	
	public String generateRefreshToken(String username, List<String> claims) {
		return JWT.create()
				 .withSubject(username)
				//  .withExpiresAt(new Date(System.currentTimeMillis()+(this.duration*2)))
				 .withIssuer(this.issuer)
				 .withClaim("roles", claims)
				 .sign(this.algorithm);
	}
	
	public String getUsernameFromToken(String token) {
		DecodedJWT decoded = this.verifier.verify(token);
		return decoded.getSubject();
	}
	
	public Collection<SimpleGrantedAuthority> getAuthoritiesFromToken(String token){
		DecodedJWT decoded = this.verifier.verify(token);
		String[] roles = decoded.getClaim("roles").asArray(String.class);
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for(String r : roles) { authorities.add(new SimpleGrantedAuthority(r)); }
		return authorities;
	}
	
	public String[] getRolesFromToken(String token){
		DecodedJWT decoded = this.verifier.verify(token);
		String[] roles = decoded.getClaim("roles").asArray(String.class);
		return roles;
	}
	
	public Boolean isTokenExpired(String token) {
    DecodedJWT decoded = this.verifier.verify(token);
    Date expiration = decoded.getExpiresAt();
    if (expiration == null) {
        return false; // sin fecha, no está expirado
    }
    return expiration.before(new Date());
}

	
	public String getTokenFromHeader(String header) {
		return header.substring(this.getTokenHeaderPrefix().length());
	}
	
	public String getTokenHeaderPrefix() {
		return "Bearer ";
	}
}
