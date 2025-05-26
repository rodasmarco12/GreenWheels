package es.uv.twcam.data_polucion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info; // Anotación

import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Data-Polución API", version = "1.0", description = "Microservicio encargado de la gestión de estaciones de monitoreo ambiental y sus lecturas asociadas. Permite crear, actualizar, eliminar y consultar estaciones de medición, así como registrar lecturas de contaminantes atmosféricos (NOx, SOx, VOCs, PM2.5, entre otros), y consultar los valores registrados o sus rangos temporales. Este microservicio almacena los datos en MySQL y MongoDB, y es consumido desde el microservicio `polucion-service`."))
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
