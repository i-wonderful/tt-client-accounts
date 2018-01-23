/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Strannica
 */
@Controller
@EnableAutoConfiguration
public class App {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello!!! My Friend!!! 123";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
