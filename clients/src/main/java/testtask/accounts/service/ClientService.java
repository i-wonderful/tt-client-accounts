package testtask.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.exception.ClientException;
import static testtask.accounts.exception.MicroserviceException.*;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;

import java.util.List;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@SuppressWarnings("JavaDoc")
@Service
@Slf4j
public class ClientService {

    private final ClientRepository repository;

    private final AccountMksService accountsMksService;
    
    @Autowired
    public ClientService(AccountMksService accMksService, ClientRepository repository) {
        this.repository = repository;
        this.accountsMksService = accMksService;
    }

    /**
     * Find Client By id
     *
     * @param id
     * @return
     */
    public Client findOne(Long id) {
        ClientEntity entity = repository.findOne(id);
        if (entity == null) {
            throw new ClientException(id, ErrorTypes.not_found);
        }
        return ClientConverter.entityToModel(entity);
    }

    /**
     * Find Client with accounts.
     *
     * @param id
     * @return
     */
    public Client findWithAccounts(Long id) {

        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Client Id must be not null.");
        }

        Client client = findOne(id);
        List<Account> accounts = accountsMksService.findAccountsByClientId(id);
        client.setAccounts(accounts);
        return client;
    }

    /**
     * Create new Client.
     *
     * @param client
     * @return
     */
    public Client create(Client client) {
        if (client == null) {
            throw new ClientException(ErrorTypes.validation, "Null client not allowed");
        }
        
        if (client.getId() != null) {
            throw new ClientException(client.getId(), ErrorTypes.validation, "Can't create client with predefined id");
        }
        
        // todo
        accountsMksService.createAccounts(client.getAccounts());
        
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     * Update Client.
     *
     * @param id
     * @param client
     * @return
     */
    public Client update(Long id, Client client) {

        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Can't update, id must be not null.");
        }

        if (!repository.exists(id)) {
            throw new ClientException(client.getId(), ErrorTypes.not_found);
        }

        // todo
        accountsMksService.updateAccounts(client.getAccounts());
        
        client.setId(id);
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     * Delete Client with Accounts.
     * 
     * @param id
     */
    public void delete(Long id) {
        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Can't delete, id must be not null");
        }

        if (!repository.exists(id)) {
            throw new ClientException(id, ErrorTypes.not_found);
        }

        accountsMksService.deleteAccountsByClientId(id);
               
        repository.delete(id);
    }
}
