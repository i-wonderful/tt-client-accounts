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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.service.ClientService;

/**
 *
 * @author Strannica
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientSErvice) {
        this.clientService = clientSErvice;
    }


//    @GetMapping(value = "/current")
//    public Client get() {
//
//        List<Account> accounts = new ArrayList<>();
////        accounts.add(new Account(new BigDecimal(1233.44)));
////        accounts.add(new Account(new BigDecimal(45566.55)));
//
//        Client cl = new Client();
//        cl.setFirstName("John");
//        cl.setLastName("Doe");
//        cl.setAccounts(accounts);
//
//        return cl;
//    }

    @GetMapping(value = "/{id}")
    public Client get(@PathVariable Long id) {
        return clientService.findOne(id);
    }

    @GetMapping(value = "/withAccounts/{id}")
    public Client findWithAccounts(@PathVariable Long id) {
        return clientService.findWithAccounts(id);
    }
    
    @PostMapping()
    public ResponseEntity<Client> create(@RequestBody Client client) {
        client = clientService.create(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

//    @PutMapping("/save")
//    public Long save() {
//        return clientService.create(client);
//    }

}
