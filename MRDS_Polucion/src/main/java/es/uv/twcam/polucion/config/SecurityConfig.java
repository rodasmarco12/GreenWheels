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

                                                // Documentación pública
                                                .pathMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/api/v1/polucion/webjars/**",
                                                                "/api/v1/polucion/estaciones",
                                                                "/api/v1/polucion/estacion/{id}/status",
                                                                "/api/v1/polucion/estacion/{id}/status-range",
                                                                "/api/v1/polucion/estacion/lecturas")
                                                .permitAll()

                                                // Endpoints protegidos por rol
                                                .pathMatchers("/api/v1/polucion/estacion").hasRole("ADMIN")
                                                .pathMatchers("/api/v1/polucion/estacion/{id}").hasRole("ESTACION") // POST
                                                .pathMatchers("/api/v1/polucion/estacion/{id}").hasRole("ADMIN") // DELETE
                                                                                                                 // y
                                                                                                                 // PUT
                                                                                                                 // también
                                                // usan esta ruta base

                                                .anyExchange().authenticated())
                                .build();
        }

        @Bean
        public ReactiveAuthenticationManager reactiveAuthenticationManager() {
                return authentication -> Mono.empty(); // Stateless
        }
}
