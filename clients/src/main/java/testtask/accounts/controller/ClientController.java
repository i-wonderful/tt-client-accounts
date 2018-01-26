package testtask.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.model.Client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
   
    @GetMapping(value = "/{id}")
    public ResponseEntity<Client> get(@PathVariable Long id) {
        Client client = clientService.findOne(id);
        HttpStatus status = (client != null) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>( client, status );
    }

    @GetMapping(value = "/withAccounts/{id}")
    public ResponseEntity<Client> findWithAccounts(@PathVariable Long id) {
        Client client = clientService.findWithAccounts(id);
        HttpStatus status = (client != null) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity(client, status);
    }

    @PostMapping()
    public ResponseEntity<Client> create(@RequestBody Client client) {
        client = clientService.save(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

//    @PutMapping("/save")
//    public Long save() {
//        return clientService.create(client);
//    }
}
