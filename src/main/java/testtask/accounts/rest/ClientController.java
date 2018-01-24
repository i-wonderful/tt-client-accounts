/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;

/**
 *
 * @author Strannica
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @GetMapping(value = "/go")
    public String hello() {
        return "I'm client controller!";
    }

    @GetMapping(value = "/current")
    public Client get() {

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(new BigDecimal(1233.44)));
        accounts.add(new Account(new BigDecimal(45566.55)));

        Client cl = new Client("John", "Doe", accounts);

        return cl;
    }
}
