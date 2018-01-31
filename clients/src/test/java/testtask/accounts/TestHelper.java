
package testtask.accounts;

import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.ClientException;

import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
public class TestHelper {

    /**
     * Create Matcher for not found exception with standard message.
     *
     * @param notExistedId
     * @return
     */
    public static CustomMatcher<ClientException> expNotFoundMatcher(final long notExistedId) {
        return new CustomMatcher<ClientException>("Check ClientException for not found client.") {
            @Override
            public boolean matches(Object o) {

                if (o instanceof ClientException == false) {
                    return false;
                }

                ClientException ex = (ClientException) o;
                return ex.getClientId().equals(notExistedId)
                        && ex.getType().equals(ErrorTypes.not_found)
                        && ex.getInfo().equals(ClientException.getStandartInfo(ErrorTypes.not_found, notExistedId));
            }
        };
    }

    /**
     * Create Matcher for validation exception.
     *
     * @return
     */
    public static CustomMatcher<ClientException> expNullArgMatcher() {
        return expMatcher(ErrorTypes.null_argument);
    }

    /**
     * Create Matcher for null argument exception.
     *
     * @return
     */
    public static CustomMatcher<ClientException> expValidationMatcher() {
        return expMatcher(ErrorTypes.validation);
    }

    /**
     * Create Matcher for bad request to mks exceptions.
     *
     * @return
     */
    public static CustomMatcher<ClientException> expBadMksRequestMatcher() {
        return expMatcher(ErrorTypes.bad_mks_request);
    }

    /**
     * Create Matcher for ClientException.
     *
     * @param errType
     * @return
     */
    public static CustomMatcher<ClientException> expMatcher(ErrorTypes errType) {
        return new CustomMatcher<ClientException>("Check ClientException for type " + errType.name()) {
            @Override
            public boolean matches(Object o) {
                if (o instanceof ClientException == false) {
                    return false;
                }
                return ((ClientException) o).getType().equals(errType);
            }
        };
    }
}
