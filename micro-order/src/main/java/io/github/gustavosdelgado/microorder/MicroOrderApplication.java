package io.github.gustavosdelgado.microorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.gustavosdelgado")
public class MicroOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroOrderApplication.class, args);
	}

}
