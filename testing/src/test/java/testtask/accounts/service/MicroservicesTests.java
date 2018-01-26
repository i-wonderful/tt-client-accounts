/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.service;

import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import testtask.accounts.AccountsApplication;
import testtask.accounts.ClientsApplication;
import testtask.accounts.model.Account;
import testtask.accounts.service.ClientService;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 25, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {AccountsApplication.class, ClientsApplication.class})
public class MicroservicesTests {

    @Autowired
    private ClientService clientService;

    @Test
    public void blob() {
        Assert.assertTrue(true);
    }

//    @Test
////    @Ignore
//    public void testRestApi() {
//
//        String answer = clientService.testRestClient();
//
//        System.out.println(answer);
//        Assert.assertNotNull(answer);
//    }
    @Test
//    @Ignore
    public void testRestGetAccounts() {
        List<Account> accounts = clientService.getAccounts(new Long(1));

        // todo
        System.out.println(accounts);

        Assert.assertNotNull(accounts);
    }

}
