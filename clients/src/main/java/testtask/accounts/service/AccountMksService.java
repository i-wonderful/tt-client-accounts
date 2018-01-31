package testtask.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.exception.ClientException;
import testtask.accounts.model.Account;

import java.util.List;

import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

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
    private String URL_HOST;

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
    public List<Account> findAccountsByClientId(Long clientId) throws ClientException {
        String url = getBaseAccountUrl() + "/ClientId/" + clientId;

        log.info("Mks Request: find accounts by clientId, url: " + url);

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return parseResponse(response, List.class);
        } else {
            throw createClientException(response, url);
        }

    }

    /**
     * Delete Accounts by clientId.
     *
     * @param clientId
     */
    public void deleteAccountsByClientId(Long clientId) throws ClientException {
        String url = getBaseAccountUrl() + "/clientId/" + clientId;

        log.info("Mks Request: delete accounts by clientId, url: {}", url);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw createClientException(response, url);
        }
    }

    public void createAccounts(List<Account> accounts) {
        // todo
    }

    public void updateAccounts(List<Account> accounts) {
        // todo
    }

    private ClientException createClientException(ResponseEntity<Object> response, String url) throws ClientException {
        String message = String.format("Mks Accounts Error, url: %s.", url);
        if (response.getBody() != null) {
            if (response.getBody() instanceof ApiErrorDto) {
                ApiErrorDto errorDto = (ApiErrorDto) response.getBody();
                message += errorDto.toString();
            } else {
                message += "Unknown response body: " + response.getBody().getClass();
                log.error(message);
            }
        } else {
            message += "ErrorDto is null";
        }

        log.error(message);
        return new ClientException(ErrorTypes.bad_mks_request, message);
    }

    private <T extends Object> T parseResponse(ResponseEntity<Object> response, Class<T> clazz) {

        if (response.getBody() == null) {
            throw new ClientException(ErrorTypes.mks_response_null);
        }

        if (clazz.isInstance(response.getBody())) {
            T obj = (T) response.getBody();
            return obj;
        } else {
            log.error("body: " + response.getBody());
            throw new ClientException(ErrorTypes.mks_response_unknown);
        }
    }

    private String getBaseAccountUrl() {
        return URL_HOST + ":" + PORT + URL_ACCOUNTS;
    }

}
