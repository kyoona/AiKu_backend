package konkuk.aiku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AikuApplication {

	public static void main(String[] args) {
		SpringApplication.run(AikuApplication.class, args);
	}

}
