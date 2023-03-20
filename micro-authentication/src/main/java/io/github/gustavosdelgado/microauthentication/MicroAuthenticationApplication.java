package io.github.gustavosdelgado.microauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.gustavosdelgado")
public class MicroAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroAuthenticationApplication.class, args);
	}

}
