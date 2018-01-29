package testtask.accounts.exception;


/**
 * Created by Alex Volobuev on 26.01.2018.
 */
public class AccountException extends MicroserviceException {

    public AccountException(RuntimeException ex) {
        super(ex);
    }
    
    public AccountException(ErrorTypes errorTypes, String string) {
        super(errorTypes, string);
    }
    
}
