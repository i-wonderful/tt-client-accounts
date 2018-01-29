package testtask.accounts.exception;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
@ControllerAdvice
@RestController
public class ClientExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiErrorDto handleBaseException(RuntimeException ex, HttpServletResponse responce) {

        ClientException out;
        if (ex instanceof ClientException) {
            out = (ClientException) ex;
        } else {
            out = new ClientException(ex);
        }

        ApiErrorDto apiErrorDto = new ApiErrorDto(out);
        responce.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ApiErrorDto handleResourceException(ResourceAccessException ex, HttpServletResponse responce) {

        ApiErrorDto apiErrorDto = new ApiErrorDto(ex);
        responce.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }
}
