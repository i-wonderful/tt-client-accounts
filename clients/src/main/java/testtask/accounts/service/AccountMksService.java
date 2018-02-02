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

import java.util.Arrays;
import java.util.List;

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

    /**
     * Find Accounts by ClientId
     *
     * @param clientId
     * @return
     */
    public List<Account> findAccountsByClientId(Long clientId) throws ClientException, RestClientResponseException {
        String url = getBaseAccountUrl( "/ClientId/" + clientId);

        log.info("Mks Request: find accounts by clientId, url: " + url);

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            Account[] accounts = MksUtil.parseResponse(response.getBody(), Account[].class);
            return Arrays.asList(accounts);
        } else {
            throw MksUtil.createClientExceptionFromResponseError(response.getBody(), url);
        }

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
     * @param accounts
     * @return
     */
    public List<Account> createAccounts(List<Account> accounts) throws ClientException {
        if (accounts.isEmpty()) {
            return accounts;
        }

        String url = getBaseAccountUrl("/list");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("Mks Request: create accounts, url: {} ", url);
        HttpEntity<List<?>> requestEntity = new HttpEntity<>(accounts, headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            Account[] accountsSaved = MksUtil.parseResponse(response.getBody(), Account[].class);
            return Arrays.asList(accountsSaved);
        } else {
            throw MksUtil.createClientExceptionFromResponseError(response.getBody(), url);
        }
    }

    public void updateAccounts(List<Account> accounts) {
        // todo
    }


    private String getBaseAccountUrl(String path) {
        UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
        uri.scheme("http").host(HOST).port(PORT).path(URL_ACCOUNTS);

        return uri.toUriString() + ((path != null) ? path : "");
    }

}
