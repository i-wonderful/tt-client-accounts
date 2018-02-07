package testtask.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.exception.ClientException;
import testtask.accounts.model.Account;

import java.util.Collections;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

import testtask.accounts.util.MksUtil;

/**
 * Rest Client for Accounts Mks.
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@Service
@Slf4j
public class AccountMksService {

    @Value("${acc-mks.server.port}")
    private String PORT;

    @Value("${acc-mks.host}")
    private String HOST;

    @Value("${acc-mks.base.url}")
    private String URL_ACCOUNTS;

    @Autowired
    private RestTemplate restTemplate;

    private final ParameterizedTypeReference<List<Account>> typeAccountList = new ParameterizedTypeReference<List<Account>>() {
    };

    /**
     * Find Accounts by ClientId
     *
     * @param clientId
     * @return
     */
    public List<Account> findAccountsByClientId(Long clientId) throws ClientException, RestClientResponseException {
        String url = getBaseAccountUrl("/ClientId/" + clientId);
        log.info("Mks Request: find accounts by clientId, url: " + url);

        return sendRequest(url, HttpEntity.EMPTY, HttpMethod.GET, typeAccountList);

    }

    /**
     * Delete Accounts by clientId.
     *
     * @param clientId
     */
    public void deleteAccountsByClientId(Long clientId) throws ClientException, RestClientException {
        String url = getBaseAccountUrl("/clientId/" + clientId);
        log.info("Mks Request: delete accounts by clientId, url: {}", url);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw MksUtil.createClientExceptionFromResponseError(response.getBody(), url);
        }
    }

    /**
     * Create Accounts
     *
     * @param clientId
     * @param accounts
     * @return
     */
    public List<Account> createAccounts(Long clientId, List<Account> accounts) throws ClientException {
        if (accounts == null || accounts.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        accounts.forEach(acc -> {
            acc.setClientId(clientId);
        });

        String url = getBaseAccountUrl("/list");
        log.info("Mks Request: create accounts, url: {} ", url);

        return sendRequest(url, accounts, HttpMethod.POST, typeAccountList);
    }

    /**
     *
     * @param clientId
     * @param accounts
     * @return
     */
    public List<Account> updateAccounts(Long clientId, List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        String url = getBaseAccountUrl("/list/client/" + clientId);
        log.info("Mks Request: update accounts, url: {} ", url);

        return sendRequest(url, accounts, HttpMethod.PUT, typeAccountList);

    }
    
     private <T extends Object> List<T> sendRequest(String url, Object content, HttpMethod method, ParameterizedTypeReference<List<T>> typeReference) {
         return sendRequest(url, wrapToSend(content), method, typeReference);
     }

    private <T extends Object> List<T> sendRequest(String url, HttpEntity<?> requestEntity, HttpMethod method, ParameterizedTypeReference<List<T>> typeReference) {
        ResponseEntity<List<T>> response = restTemplate.exchange(url, method, requestEntity, typeReference);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        } else {
            throw MksUtil.createClientExceptionFromResponseError(response.getBody(), url);
        }
    }

    private String getBaseAccountUrl(String path) {
        UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
        uri.scheme("http").host(HOST).port(PORT).path(URL_ACCOUNTS);

        return uri.toUriString() + ((path != null) ? path : "");
    }

    private HttpEntity<?> wrapToSend(Object content) {

        if (content == null) {
            return HttpEntity.EMPTY;
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(content, headers);
        return requestEntity;
    }

}
