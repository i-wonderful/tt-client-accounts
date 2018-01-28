package testtask.accounts.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
public class ApiErrorDto {

    private HttpStatus status;
    private String message;
    private String errType;

    public ApiErrorDto() {
    }

    public ApiErrorDto (MicroserviceException.ErrorTypes type, String info) {
        switch (type) {
            case business:
                status = HttpStatus.CONFLICT;
                break;
            case validation:
                status = HttpStatus.NOT_ACCEPTABLE;
                break;
            case not_found:
                status = HttpStatus.NOT_FOUND;
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
}