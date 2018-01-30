/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import static testtask.accounts.exception.MicroserviceException.*;
import testtask.accounts.model.Account;

/**
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

    public List<Account> findAccountsByClientId(Long clientId) {
        String path = getBaseAccountUrl() + "/ClientId/" + clientId;

        log.info("Get client with accounts by url: " + path);

        ResponseEntity<Account[]> responce = restTemplate.getForEntity(path, Account[].class);

        // todo check Exceptions
        List<Account> accounts = Arrays.asList(responce.getBody());
        return accounts;
    }

    /**
     * Delete Accounts by clientId.
     *
     * @param clientId
     * @return
     */
    public int deleteAccountsByClientId(Long clientId) {
        String url = getBaseAccountUrl() + "/clientId/" + clientId;

        log.info("delete accounts by clientId, url: {}", url);

        ResponseEntity<ApiErrorDto> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, ApiErrorDto.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return 1;
        } else {
            String message = String.format("Error in mks accounts. url: %s ", url);
            
            ApiErrorDto errorMks = response.getBody();
            message += (errorMks != null) ? errorMks.toString() : "";
            log.error(message);
            throw new ClientException(clientId, ErrorTypes.bad_mks_request, message);
        }
    }

    private String getBaseAccountUrl() {
        return URL_HOST + ":" + PORT + URL_ACCOUNTS;
    }

}
