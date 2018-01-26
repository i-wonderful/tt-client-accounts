package testtask.accounts.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.model.Account;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
public class ClientService {

    private static final String URL_ACCOUNTS = "http://localhost:8081/accounts";
    
    @Autowired
    private RestTemplate restTemplate;

    public String testRestClient() {
        return restTemplate.getForObject(URL_ACCOUNTS + "/go", String.class);
    }

    
   
    /**
     * Get from REST api
     *
     * @param clientId
     * @return
     */
    public List<Account> getAccounts(Long clientId) {

        // todo for tests
        List<Account> accounts = restTemplate.getForObject(URL_ACCOUNTS + "/all", List.class);
        return accounts;
    }
}
