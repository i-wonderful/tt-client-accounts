package testtask.accounts.exception;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
public class ClientException extends MicroserviceException {

    private final Long clientId;

    public ClientException(RuntimeException ex) {
        super(ex);
        clientId = null;
    }
    
    public ClientException(final Long clientId, ErrorTypes type) {
        super(type, getStandartInfo(type, clientId));
        this.clientId = clientId;
    }

    public ClientException(ErrorTypes type, String info) {
        super(type, info);
        this.clientId = null;
    }

    public ClientException(Long clientId, ErrorTypes type, String info) {
        super(type, info);
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public static String getStandartInfo(ErrorTypes type, Long clientId) {
        String errorMessage = "Error Message";
        switch (type) {
            case not_found:
                errorMessage = "Not found client";
                break;
            case business:
                errorMessage = "Busness Error";
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
        errorMessage += clientId != null ? ", clientId = " + clientId : "";
        return errorMessage;
    }
}
