package testtask.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@SpringBootApplication
public class Application {

    final Logger logger = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    
//    @Bean
//    public CommandLineRunner run(RestTemplate restTemplate) {
//
//        return (strings) -> {
//            String answer = restTemplate.getForObject("http://localhost:8080/client/go", String.class);
//            logger.info(answer);
//        };
//    }

}
