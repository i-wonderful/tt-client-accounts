package testtask.accounts;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
@ControllerAdvice
@RestController
public class CommonExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ApiError handleBaseException(RuntimeException ex){

        AccountException e;

        if (ex instanceof AccountException) {
            e = (AccountException) ex;
        } else {
            e = new AccountException(ex);
        }

        return new ApiError(e);
    }

}