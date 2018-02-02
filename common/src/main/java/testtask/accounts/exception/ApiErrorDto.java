package testtask.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
public class ApiErrorDto {

    private HttpStatus status;
    private String message;
    private String errType;

    public ApiErrorDto() {
    }

    public ApiErrorDto(MicroserviceException.ErrorTypes type, String info) {
        switch (type) {
            case business:
                status = HttpStatus.CONFLICT;
                break;
            case validation:
                status = HttpStatus.NOT_ACCEPTABLE;
                break;
            case null_argument:
                status = HttpStatus.PARTIAL_CONTENT;
                break;
            case not_found:
                status = HttpStatus.NOT_FOUND;
                break;
            case bad_mks_request:
                status = HttpStatus.BAD_REQUEST;
                break;
            case db_error:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            case other:
                status = HttpStatus.BAD_REQUEST;
                break;
        }
        this.message = info;
        this.errType = type.name();
    }

    public ApiErrorDto(MicroserviceException e) {
        this(e.getType(), e.getInfo());
    }

    public ApiErrorDto(ResourceAccessException exp) {
        this(MicroserviceException.ErrorTypes.bad_mks_request, exp.getLocalizedMessage());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    @Override
    public String toString() {
        return  (errType != null ? ", errorType: " + errType : "")
                + (message != null ? ", message: " + message : "");
    }

}
