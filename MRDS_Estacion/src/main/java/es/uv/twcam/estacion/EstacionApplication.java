package es.uv.twcam.estacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EstacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstacionApplication.class, args);
	}

}
