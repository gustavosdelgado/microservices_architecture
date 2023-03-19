package io.github.gustavosdelgado.microauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.gustavosdelgado")
@EnableJpaRepositories("io.github.gustavosdelgado.library.domain")
@EntityScan("io.github.gustavosdelgado.library.domain")
public class MicroAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroAuthenticationApplication.class, args);
	}

}
