package io.github.gustavosdelgado.microrestaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.gustavosdelgado")
@EnableJpaRepositories("io.github.gustavosdelgado.library.domain")
@EntityScan("io.github.gustavosdelgado.library.domain")
public class MicrorestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrorestaurantApplication.class, args);
	}

}
