package es.uv.twcam.polucion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import es.uv.twcam.polucion.security.JwtSecurityContext;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                        JwtSecurityContext context) {
                return http
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                                .formLogin(formLoginSpec -> formLoginSpec.disable())
                                .logout(logoutSpec -> logoutSpec.disable())
                                .cors(corsSpec -> corsSpec.disable())
                                .securityContextRepository(context)
                                .authorizeExchange(exchanges -> exchanges
                                                .pathMatchers(
                                                                "/webjars/**", // 🔥 necesario para Swagger UI
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**", // si lo estás usando también
                                                                "/api/v1/polucion/webjars/swagger-ui/**",
                                                                "/api/v1/polucion/api-gui.html", // ruta personalizada
                                                                "/api/v1/polucion/api-spec/**",
                                                                "/api/v1/polucion/estaciones",
                                                                "/api/v1/polucion/estacion/*/status",
                                                                "/api/v1/polucion/estacion/*/status-range",
                                                                "/api/v1/polucion/estacion/estadistica",
                                                                "/api/v1/polucion/estadisticas/medias")
                                                .permitAll()
                                                .anyExchange().authenticated())
                                .build();
        }

        @Bean
        public ReactiveAuthenticationManager reactiveAuthenticationManager() {
                return authentication -> Mono.empty(); // Stateless
        }
}
