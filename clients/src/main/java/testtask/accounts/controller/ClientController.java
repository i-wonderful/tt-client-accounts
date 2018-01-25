/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;

import java.util.ArrayList;
import java.util.List;
import testtask.accounts.dao.ClientConverter;

/**
 *
 * @author Strannica
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    // тут будет сервис. да.
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping(value = "/go")
    public String hello() {
        return "I'm client controller!";
    }

    @GetMapping(value = "/current")
    public Client get() {

        List<Account> accounts = new ArrayList<>();
//        accounts.add(new Account(new BigDecimal(1233.44)));
//        accounts.add(new Account(new BigDecimal(45566.55)));

        Client cl = new Client();
        cl.setFirstName("John");
        cl.setLastName("Doe");
        cl.setAccounts(accounts);

        return cl;
    }

    @GetMapping(value = "/{id}")
    public Client get(@PathVariable Long id) {
        return ClientConverter.entityToModel(clientRepository.findOne(id));
    }
}
