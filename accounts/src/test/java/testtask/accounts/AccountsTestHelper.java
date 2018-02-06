
package testtask.accounts;

import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.AccountException;
import static testtask.accounts.exception.MicroserviceException.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
public class AccountsTestHelper {

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
}
