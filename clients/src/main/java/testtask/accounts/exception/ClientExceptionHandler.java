package testtask.accounts.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
@ControllerAdvice
@RestController
public class ClientExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiErrorDto handleBaseException(RuntimeException ex, HttpServletResponse response) {

        ClientException out;
        if (ex instanceof ClientException) {
            out = (ClientException) ex;
        } else {
            out = new ClientException(ex);
        }

        ApiErrorDto apiErrorDto = new ApiErrorDto(out);
        response.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ApiErrorDto handleResourceException(ResourceAccessException ex, HttpServletResponse response) {

        ApiErrorDto apiErrorDto = new ApiErrorDto(ex);
        response.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }

    @ExceptionHandler(RestClientException.class)
    public ApiErrorDto handleHttpErrorException(RestClientException ex, HttpServletResponse response) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(MicroserviceException.ErrorTypes.bad_mks_request, ex.getMessage());
        response.setStatus(apiErrorDto.getStatus().value());
        return apiErrorDto;
    }

    @ExceptionHandler(DataAccessException.class)
    public ApiErrorDto handleDbException(DataAccessException ex, HttpServletResponse response) {

        ApiErrorDto apiErrorDto = new ApiErrorDto(MicroserviceException.ErrorTypes.db_error, ex.getLocalizedMessage());

        response.setStatus(apiErrorDto.getStatus().value());

        return apiErrorDto;
    }
}
