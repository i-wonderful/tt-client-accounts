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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import testtask.accounts.AccountsApplication;
import testtask.accounts.ClientsApplication;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Client;
import testtask.accounts.model.Currency;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 28, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {ClientsApplication.class, AccountsApplication.class})
@TestPropertySource(locations = "classpath:mks-testing.properties")
@WithMockUser
public class MksServiceIntegrationTests {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;
    
    ClientEntity clientEntity;
    List<AccountEntity> accounts;

    @Before
    public void init() {
        clientEntity = new ClientEntity();
        clientEntity.setFirstName("James");
        clientEntity.setLastName("Cameron");
        clientEntity = clientRepository.save(clientEntity);

        AccountEntity account1 = new AccountEntity();
        account1.setName("Main Acc");
        account1.setCurrency(Currency.USD);
        account1.setBalance(new BigDecimal(543757));
        account1.setClientId(clientEntity.getId());
//        accountRepository.save(account1);

        AccountEntity account2 = new AccountEntity();
        account2.setName("Film Budget");
        account2.setCurrency(Currency.USD);
        account2.setBalance(new BigDecimal(754555.95));
        account2.setClientId(clientEntity.getId());

        accounts = new ArrayList<>();
        accountRepository.save(new ArrayList<AccountEntity>() {
            {
                add(account1);
                add(account2);
            }
        }).forEach(accounts::add);

    }

    @Test
    public void blob() {

        Assert.assertTrue(true);
    }

    @Test
    public void findClientWithAccounts() {
        Client client = clientService.findWithAccounts(clientEntity.getId());
        
        Assert.assertNotNull(client);
    }

}
