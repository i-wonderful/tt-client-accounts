package testtask.accounts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.serializator.JsonBigDecimalSerializer;

import java.math.BigDecimal;

/**
 *
 */
@SpringBootApplication
public class AccountsApplication {

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
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//        mapper.configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, true);
        return mapper;
    }

}
