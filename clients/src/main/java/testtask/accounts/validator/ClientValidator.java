
package testtask.accounts.validator;

import org.springframework.stereotype.Component;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Client;
import testtask.accounts.validation.BaseValidator;

/**
 *
 * @author Strannica
 */
@Component
public class ClientValidator extends BaseValidator<Client, ClientException>{

    @Override
    public void validateItem(Client item) {
        // todo
    }

    @Override
    protected ClientException createException(MicroserviceException.ErrorTypes errorType, String message) {
        return new ClientException(errorType, message);
    }
    
}
