package es.uv.twcam.polucion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

@SpringBootApplication
public class PolucionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolucionApplication.class, args);
	}

}
