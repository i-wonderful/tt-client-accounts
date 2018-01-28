/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.util.Lists;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import testtask.accounts.AccountsApplication;
import testtask.accounts.ClientsApplication;
import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;
import testtask.accounts.model.Currency;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 28, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {ClientsApplication.class, AccountsApplication.class})
@TestPropertySource(locations = "classpath:application.properties")
@WithMockUser
public class MksServiceIntegrationTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    ClientEntity clientEntity;
    Client client;
    List<AccountEntity> accountsEntities;
    List<Account> accounts;

    @Before
    public void init() {
//        System.setProperty("server.port", "7780");
        
        clientEntity = new ClientEntity();
        clientEntity.setFirstName("James");
        clientEntity.setLastName("Cameron");
        clientEntity = clientRepository.save(clientEntity);

        AccountEntity account1 = new AccountEntity();
        account1.setName("Main Acc");
        account1.setCurrency(Currency.USD);
        account1.setBalance(new BigDecimal(543757));
        account1.setClientId(clientEntity.getId());

        AccountEntity account2 = new AccountEntity();
        account2.setName("Film Budget");
        account2.setCurrency(Currency.USD);
        account2.setBalance(new BigDecimal(754555.95));
        account2.setClientId(clientEntity.getId());

        accountsEntities = new ArrayList<>();
        accountRepository.save(new ArrayList<AccountEntity>() {
            {
                add(account1);
                add(account2);
            }
        }).forEach(accountsEntities::add);

        client = ClientConverter.entityToModel(clientEntity);
        accounts = AccountConvertor.entityListToModels(accountsEntities);
    }

    @Test
    public void blob() {

       assertTrue(true);
    }

    @Test
    public void findClientWithAccounts() {
        Client clientFind = clientService.findWithAccounts(client.getId());

        assertNotNull(clientFind);
        assertNotNull(clientFind.getAccounts());

        // check client
        assertEquals(client.getFirstName(), clientFind.getFirstName());
        assertEquals(client.getLastName(), clientFind.getLastName());

        // check accounts
        assertEquals(accounts.size(), clientFind.getAccounts().size());

    }

}
