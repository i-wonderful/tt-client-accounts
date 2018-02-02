package testtask.accounts;

import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.MicroserviceException;

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
}
