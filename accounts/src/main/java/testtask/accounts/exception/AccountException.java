package testtask.accounts.exception;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
public class AccountException extends MicroserviceException {

    private final Long accountId;

    public AccountException(RuntimeException ex) {
        super(ex);
        this.accountId = null;
    }

    public AccountException(ErrorTypes errorTypes, String string) {
        super(errorTypes, string);
        this.accountId = null;
    }

    public AccountException(ErrorTypes errorTypes) {
        this(errorTypes, getStandartInfo(errorTypes, null));
    }

    public AccountException(ErrorTypes type, Long accountId) {
        super(type, getStandartInfo(type, accountId));
        this.accountId = accountId;
    }

    public static String getStandartInfo(ErrorTypes type, Long accountId) {

        String errorMessage = "Error Message";
        switch (type) {
            case not_found:
                errorMessage = "Not found account";
                break;
            case business:
                errorMessage = "Busness Error";
                break;
            case null_argument:
                errorMessage = "Null Argument not allowed";
                break;
            case validation:
                errorMessage = "Validation Error";
                break;
            case bad_mks_request:
                errorMessage = "Resource request exception";
                break;
            case other:
                errorMessage = "Some other Error";
                break;

        }
        errorMessage += accountId != null ? ", accountId = " + accountId : "";
        return errorMessage;
    }

    public Long getAccountId() {
        return accountId;
    }

}
