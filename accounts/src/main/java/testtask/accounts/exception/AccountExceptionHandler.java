package testtask.accounts.exception;

import testtask.accounts.exception.AccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
//import testtask.accounts.ApiErrorDto;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
@ControllerAdvice
@RestController
public class AccountExceptionHandler {


    @ExceptionHandler(value = RuntimeException.class)
    public ApiErrorDto handleBaseException(RuntimeException ex, HttpServletResponse response){

        AccountException e;
        if (ex instanceof AccountException) {
            e = (AccountException) ex;
        } else {
            e = new AccountException(ex);
        }

        ApiErrorDto apiErrorDto = new ApiErrorDto(e);
        response.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }

}