package es.uv.twcam.ayuntamiento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import es.uv.twcam.ayuntamiento.security.JwtSecurityContextRepository;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                        JwtSecurityContextRepository contextRepository) {
                return http
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                                .formLogin(formLoginSpec -> formLoginSpec.disable())
                                .logout(logoutSpec -> logoutSpec.disable())
                                .cors(corsSpec -> corsSpec.disable())
                                .securityContextRepository(contextRepository)
                                .authorizeExchange(exchanges -> exchanges
                                                .pathMatchers(
                                                                "/api/v1/documentation/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/webjars/**",
                                                                "/api/v1/auth/**",
                                                                "/api/v1/ayuntamiento/disponible-cercano", // AR1
                                                                "/api/v1/ayuntamiento/aggregatedData" // AR3

                                                )
                                                .permitAll()
                                                .pathMatchers(
                                                                "/api/v1/ayuntamiento/aparcamiento", // BR1
                                                                "/api/v1/ayuntamiento/aparcamiento/{id}" // BR2 BR3
                                                ).hasRole("ADMIN")

                                                .pathMatchers("/api/v1/ayuntamiento/aggregateData"// BR2 BR3
                                                ).hasRole("SERVICIO")

                                                .anyExchange().authenticated())
                                .build();
        }

        @Bean
        public ReactiveAuthenticationManager reactiveAuthenticationManager() {
                return authentication -> Mono.empty(); // Sigue siendo innecesario aquí
        }
}
