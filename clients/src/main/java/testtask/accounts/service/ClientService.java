package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Account;

import java.util.List;
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
     *
     * @param id
     * @return
     */
    public Client findOne(Long id) {
        return ClientConverter.entityToModel(repository.findOne(id));
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
        List<Account> accounts = restTemplate.getForObject(URL_ACCOUNTS , List.class);
        return accounts;
    }

    /**
     * Save or Update item
     *
     * @param client
     * @return
     */
    public Client save(Client client) {
        return ClientConverter.entityToModel(repository.save(ClientConverter.modelToEntity(client)));
    }

    /**
     * 
     * @param client
     * @return 
     */
//    public Client update(Client client) {
//        ClientEntity e = repository.findOne(client.getId());
////        e.set
//        return ClientConverter.entityToModel(repository.save(e));
//    }
    
    /**
     * 
     * @param id 
     */
    public void delete(Long id) {
        // todo delete accounts
        repository.delete(id);
    }
}
