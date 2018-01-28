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

//    private ErrorTypes type;
//    private String info;
//    public enum ErrorTypes {
//        business,
//        validation,
//        not_found,
//        other
//    }
//    public AccountException (RuntimeException e) {
//        type = ErrorTypes.other;
//        info = e.getLocalizedMessage();
//    }
//
//    public AccountException(ErrorTypes type, String info) {
//        this.type = type;
//        this.info = info;
//    }
//    public ErrorTypes getType() {
//        return type;
//    }
//
//    public void setType(ErrorTypes type) {
//        this.type = type;
//    }
//
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }

    
}
