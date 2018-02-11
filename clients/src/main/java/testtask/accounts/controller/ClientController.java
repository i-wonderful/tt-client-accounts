package testtask.accounts.controller;

import io.swagger.annotations.Api;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.model.Client;
import testtask.accounts.service.ClientService;

/**
 *
 * @author Strannica
 */
@RestController
@RequestMapping(value = "/clients")
@Api(value = "clients", description = "Rest API for clients operations", tags = "Clients API")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientSErvice) {
        this.clientService = clientSErvice;
    }

    @GetMapping
    public final String hola() throws UnknownHostException {
        return "Hola! Puedes encontrarme en " + InetAddress.getLocalHost().getHostAddress();
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<Client> get(@PathVariable Long id) {
        Client client = clientService.findOne(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping(value = "/withAccounts/{id}")
    public ResponseEntity<Client> findWithAccounts(@PathVariable Long id) {
        Client client = clientService.findWithAccounts(id);
        return new ResponseEntity(client, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client) {
        client = clientService.create(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        client = clientService.update(id, client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        clientService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
