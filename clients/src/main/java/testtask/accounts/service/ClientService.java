package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Account;

import java.util.List;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Client;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
public class ClientService {

    private static final String URL_ACCOUNTS = "http://localhost:8081/accounts";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClientRepository repository;

    /**
     * Find Client By id
     *
     * @param id
     * @return
     */
    public Client findOne(Long id) {
        ClientEntity entity = repository.findOne(id);
        if (entity == null) {
            throw new ClientException(id, MicroserviceException.ErrorTypes.not_found);
        }
        return ClientConverter.entityToModel(entity);
    }

    /**
     *
     * @param id
     * @return
     */
    public Client findWithAccounts(Long id) {

        Client client = findOne(id);
        List<Account> accounts = getAccounts(id);
        client.setAccounts(accounts);
        return client;
    }

    /**
     * Get from REST api
     *
     * @param clientId
     * @return
     */
    public List<Account> getAccounts(Long clientId) {

        // todo for tests
        List<Account> accounts = restTemplate.getForObject(URL_ACCOUNTS, List.class);
        return accounts;
    }

    /**
     * Save or Update item
     *
     * @param client
     * @return
     */
    public Client create(Client client) {
        if (client.getId() != null) {
            throw new ClientException(client.getId(), MicroserviceException.ErrorTypes.validation, "Can't create client with predefined id");
        }
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     *
     * @param client
     * @return
     */
    public Client update(Client client) {

        if (client.getId() == null) {
            throw new ClientException(MicroserviceException.ErrorTypes.validation, "Can't update, id must be not null.");
        }

        if(!repository.exists(client.getId()))
            throw new ClientException(client.getId(), MicroserviceException.ErrorTypes.not_found);
        
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     *
     * @param id
     */
    public void delete(Long id) {
        if (id == null) {
            throw new ClientException(MicroserviceException.ErrorTypes.validation, "Can't update, id must be not null");
        }

        if (repository.exists(id) == false) {
            throw new ClientException(id, MicroserviceException.ErrorTypes.not_found);
        }
        
        // todo delete accounts
        repository.delete(id);
    }
}
