package es.uv.twcam.servicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioApplication.class, args);
	}

}
