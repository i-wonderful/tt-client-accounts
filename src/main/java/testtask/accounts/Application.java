package testtask.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.rest.AccountsController;
import testtask.accounts.rest.ClientController;

/**
 *
 */
//@Controller
@EnableAutoConfiguration
public class Application {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello!!! My Friend!!! 123";
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{Application.class, AccountsController.class, ClientController.class}, args);
    }
}
