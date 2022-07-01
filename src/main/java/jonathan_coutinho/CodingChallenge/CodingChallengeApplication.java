package jonathan_coutinho.CodingChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CodingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingChallengeApplication.class, args);
	}
}
