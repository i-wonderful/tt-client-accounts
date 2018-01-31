/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts;

import org.hamcrest.CustomMatcher;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;

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
                        && ex.getType().equals(MicroserviceException.ErrorTypes.not_found)
                        && ex.getInfo().equals(ClientException.getStandartInfo(MicroserviceException.ErrorTypes.not_found, notExistedId));
            }
        };
    }
    
    /**
     * Create Matcher for validation exception.
     * 
     * @return 
     */
    public static CustomMatcher<ClientException> expValidationMatcher(){
    return new CustomMatcher<ClientException>("Check ClientException for validation.") {
            @Override
            public boolean matches(Object o) {
                if (o instanceof ClientException == false) {
                    return false;
                }
                return ((ClientException) o).getType().equals(MicroserviceException.ErrorTypes.validation);
            }
        };
    }
}
