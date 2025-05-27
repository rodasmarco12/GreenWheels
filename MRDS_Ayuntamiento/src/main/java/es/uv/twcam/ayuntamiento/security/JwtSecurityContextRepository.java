package es.uv.twcam.ayuntamiento.security;

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
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final WebClient.Builder webClientBuilder;
    private final String authUrl;
    private final ObjectMapper objectMapper;

    public JwtSecurityContextRepository(WebClient.Builder webClientBuilder, 
                                        @Value("${auth.url}") String authUrl) {
        this.webClientBuilder = webClientBuilder;
        this.authUrl = authUrl;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        return webClientBuilder.build()
                .get()
                .uri(authUrl)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(status -> status != HttpStatus.ACCEPTED,
                        response -> Mono.error(new RuntimeException("UNAUTHORIZED")))
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        JsonNode json = objectMapper.readTree(body);
                        String userId = json.get("userId").asText();

                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        for (JsonNode roleNode : json.get("roles")) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleNode.asText()));
                        }

                        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                        return Mono.just((SecurityContext) new SecurityContextImpl(auth));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("UNAUTHORIZED"));
                    }
                })
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // No guardamos el contexto en ningún lado (stateless)
    }
}
