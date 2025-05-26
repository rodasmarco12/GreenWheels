package es.uv.twcam.polucion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info; // Anotación
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Polución API", version = "1.0", description = "Esta API permite interactuar con el sistema de monitoreo ambiental mediante estaciones de medición. Actúa como intermediario entre el cliente y el microservicio `data-polucion`, consumiendo sus endpoints a través de WebClient. Las operaciones incluyen el registro, actualización, eliminación y consulta de estaciones, así como el envío y recuperación de lecturas de sensores en tiempo real. Los endpoints de esta API requieren un header de seguridad `x-auth` para validar el acceso en operaciones que modifican recursos o consultan estadísticas."))
@SecurityScheme(name = "x-auth", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "x-auth")
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Data Access API for Pollution")
                        .version("1.0")
                        .description("API for managing pollution station data"));
    }
}
