package uv.es.twcam.aparcamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AparcamientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AparcamientoApplication.class, args);
	}

}
