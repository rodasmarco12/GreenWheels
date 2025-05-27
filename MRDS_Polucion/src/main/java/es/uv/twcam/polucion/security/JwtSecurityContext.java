package es.uv.twcam.polucion.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtSecurityContext implements ServerSecurityContextRepository {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtSecurityContext(WebClient.Builder webClientBuilder,
            @Value("${auth.url}") String authUrl) {
        this.webClient = webClientBuilder.baseUrl(authUrl).build();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        return webClient
                .get()
                .uri("/api/v1/users/authenticated")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(status -> status != HttpStatus.ACCEPTED,
                        response -> Mono.error(new RuntimeException("UNAUTHORIZED")))
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        JsonNode json = objectMapper.readTree(body);
                        String userId = json.get("userId").asText();
                        JsonNode rolesNode = json.get("roles");

                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        if (rolesNode != null && rolesNode.isArray()) {
                            for (JsonNode role : rolesNode) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.asText()));
                            }
                        }

                        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                        SecurityContext context = new SecurityContextImpl(auth);
                        return Mono.just(context);

                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error parsing authentication response"));
                    }
                })
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // Stateless
    }
}
