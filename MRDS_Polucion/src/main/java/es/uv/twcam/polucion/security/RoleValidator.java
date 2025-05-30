package es.uv.twcam.polucion.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RoleValidator {

    public Mono<Boolean> hasRole(String requiredRole) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getAuthorities().stream()
                        .anyMatch(granted -> granted.getAuthority().equals("ROLE_" + requiredRole)));
    }
}
