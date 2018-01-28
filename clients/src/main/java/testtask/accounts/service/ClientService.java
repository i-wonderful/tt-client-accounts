package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;

import java.util.List;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
public class ClientService {

    @Value("${acc-mks.server.port}")
    private String PORT;

    @Value("${acc-mks.host}")
    private String URL_HOST;

    @Value("${acc-mks.base.url}")
    private String URL_ACCOUNTS;

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
     * Find Client with accounts.
     * 
     * @param id
     * @return
     */
    public Client findWithAccounts(Long id) {

        if(id == null)
            throw new ClientException(MicroserviceException.ErrorTypes.validation, "Client Id must be not null.");
        
        
        Client client = findOne(id);
        String path = URL_HOST + ":" + PORT + URL_ACCOUNTS + "/ClientId/" + id;

        System.out.println(">> Get Client with Accounts by url " + path);

        List<Account> accounts = restTemplate.getForObject(path, List.class);
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
        if (client.getId() != null) {
            throw new ClientException(client.getId(), MicroserviceException.ErrorTypes.validation, "Can't create client with predefined id");
        }
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
            throw new ClientException(MicroserviceException.ErrorTypes.validation, "Can't update, id must be not null.");
        }

        if (!repository.exists(id)) {
            throw new ClientException(client.getId(), MicroserviceException.ErrorTypes.not_found);
        }

        client.setId(id);
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     *
     * @param id
     */
    public void delete(Long id) {
        if (id == null) {
            throw new ClientException(MicroserviceException.ErrorTypes.validation, "Can't delete, id must be not null");
        }

        if (repository.exists(id) == false) {
            throw new ClientException(id, MicroserviceException.ErrorTypes.not_found);
        }

        // todo delete accounts
        repository.delete(id);
    }
}
