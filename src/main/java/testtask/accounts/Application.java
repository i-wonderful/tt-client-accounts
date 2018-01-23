package testtask.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 */
@EnableAutoConfiguration
@ComponentScan(value = "testtask.accounts.rest")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
