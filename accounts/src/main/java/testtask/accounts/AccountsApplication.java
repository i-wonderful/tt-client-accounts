package testtask.accounts;

import testtask.accounts.serializator.JsonBigDecimalSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.serializator.JsonBigDecimalSerializer;

/**
 *
 */
@SpringBootApplication
public class AccountsApplication {

    final Logger logger = LoggerFactory.getLogger(AccountsApplication.class);
    
    public static void main(String[] args) {

        SpringApplication.run(AccountsApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public static ObjectMapper serializingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JsonComponentModule jsonBigDecimal = new JsonComponentModule();
        jsonBigDecimal.addSerializer(BigDecimal.class, new JsonBigDecimalSerializer());
        mapper.registerModule(jsonBigDecimal);
        return mapper;
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
