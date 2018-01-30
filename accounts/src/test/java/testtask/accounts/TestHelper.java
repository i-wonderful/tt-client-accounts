/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts;

import java.math.BigDecimal;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
public class TestHelper {

    public static Account createAccountModel(double balance, Currency currency, Long clientId, String name) {

        Account account = new Account();
        account.setBalance(new BigDecimal(balance));
        account.setCurrency(currency);
        account.setClientId(clientId);
        account.setName(name);

        return account;
    }
}
