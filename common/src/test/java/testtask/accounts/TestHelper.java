package testtask.accounts;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
public class TestHelper {

    /**
     * Create Matcher for validation exception.
     *
     * @return
     */
    public static CustomMatcher<MicroserviceException> expNullArgMatcher() {
        return expMatcher(ErrorTypes.null_argument);
    }

    /**
     * Create Matcher for null argument exception.
     *
     * @return
     */
    public static CustomMatcher<MicroserviceException> expValidationMatcher() {
        return expMatcher(ErrorTypes.validation);
    }

    /**
     * Create Matcher for bad request to mks exceptions.
     *
     * @return
     */
    public static CustomMatcher<MicroserviceException> expBadMksRequestMatcher() {
        return expMatcher(ErrorTypes.bad_mks_request);
    }

    /**
     * Create Matcher for MicroserviceException.
     *
     * @param errType
     * @return
     */
    public static CustomMatcher<MicroserviceException> expMatcher(final ErrorTypes errType) {
        return new CustomMatcher<MicroserviceException>("Check MicroserviceException for type " + errType.name()) {
            @Override
            public boolean matches(Object o) {
                if (o instanceof MicroserviceException == false) {
                    return false;
                }
                return ((MicroserviceException) o).getType().equals(errType);
            }
        };
    }

    /**
     *
     * @return
     */
    public static List<Account> createAccountsNullIdsList() {
        return Arrays.asList(createAccount(34545.91, Currency.RUB, 81L, "SomeAcc"),
                createAccount(4444556, Currency.RUB, 81L, "Some Acc 2"));
    }

    /**
     * 
     * @return 
     */
    public static List<Account> createAccountsNotNullIdList() {
        return Arrays.asList(createAccount(1L, 64566756, Currency.RUB, 77L, "Some Existed Acc"),
                createAccount(2L, 5467567, Currency.USD, 77L, "Some Existed Acc Two"));
    }

    /**
     *
     * @return
     */
    public static Account createAccount() {
        return createAccount(null, 558668.77, Currency.USD, 55L, "Some New Account");
    }

    /**
     * Create new Model object.
     *
     * @param balance
     * @param currency
     * @param clientId
     * @param name
     * @return
     */
    public static Account createAccount(double balance, Currency currency, Long clientId, String name) {
        return createAccount(null, balance, currency, clientId, name);
    }

    /**
     * Create Model Object
     *
     * @param id
     * @param balance
     * @param currency
     * @param clientId
     * @param name
     * @return
     */
    public static Account createAccount(Long id, double balance, Currency currency, Long clientId, String name) {

        Account account = new Account();
        account.setId(id);
        account.setBalance(new BigDecimal(balance));
        account.setCurrency(currency);
        account.setClientId(clientId);
        account.setName(name);

        return account;
    }
}
