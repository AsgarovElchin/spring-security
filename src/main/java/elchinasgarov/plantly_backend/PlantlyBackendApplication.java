package elchinasgarov.plantly_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantlyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantlyBackendApplication.class, args);
	}

}
