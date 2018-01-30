/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts;

import java.math.BigDecimal;
import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.AccountException;
import static testtask.accounts.exception.MicroserviceException.*;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
public class TestHelper {

    /**
     * Create new Model obj.
     *
     * @param balance
     * @param currency
     * @param clientId
     * @param name
     * @return
     */
    public static Account createAccountModel(double balance, Currency currency, Long clientId, String name) {

        Account account = new Account();
        account.setBalance(new BigDecimal(balance));
        account.setCurrency(currency);
        account.setClientId(clientId);
        account.setName(name);

        return account;
    }

    /**
     * Create Matcher for not found exception with standard message.
     *
     * @param notExistedId
     * @return
     */
    public static CustomMatcher<AccountException> expNotFoundMatcher(final long notExistedId) {
        return new CustomMatcher<AccountException>("Check AccountException for not found client.") {
            @Override
            public boolean matches(Object o) {

                if (o instanceof AccountException == false) {
                    return false;
                }

                AccountException ex = (AccountException) o;
                return ex.getAccountId().equals(notExistedId)
                        && ex.getType().equals(ErrorTypes.not_found)
                        && ex.getInfo().equals(AccountException.getStandartInfo(ErrorTypes.not_found, notExistedId));
            }
        };
    }

    /**
     * Create Matcher for validation exception.
     *
     * @return
     */
    public static CustomMatcher<AccountException> expNullArgMatcher() {
        return expMatcher(ErrorTypes.null_argument);
    }

    /**
     * Create Matcher for null argument exception.
     *
     * @return
     */
    public static CustomMatcher<AccountException> expValidationMatcher() {
        return expMatcher(ErrorTypes.validation);
    }

    /**
     * Create Matcher for AccountException.
     * 
     * @param errType
     * @return 
     */
    public static CustomMatcher<AccountException> expMatcher(ErrorTypes errType) {
        return new CustomMatcher<AccountException>("Check AccountException for type " + errType.name()) {
            @Override
            public boolean matches(Object o) {
                if (o instanceof AccountException == false) {
                    return false;
                }
                return ((AccountException) o).getType().equals(errType);
            }
        };
    }
}
