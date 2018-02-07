package testtask.accounts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.exception.ResponseMksErrorHandler;
import testtask.accounts.serializator.JsonBigDecimalSerializer;

/**
 *
 */
@SpringBootApplication
public class ClientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .errorHandler(new ResponseMksErrorHandler())
                .basicAuthorization("user", "123")
                .build();
    }

    @Bean
    @Primary
    public static ObjectMapper serializingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JsonComponentModule jsonBigDecimal = new JsonComponentModule();
        jsonBigDecimal.addSerializer(BigDecimal.class, new JsonBigDecimalSerializer());
        mapper.registerModule(jsonBigDecimal);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
