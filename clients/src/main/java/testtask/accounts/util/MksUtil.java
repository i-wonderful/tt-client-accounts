package testtask.accounts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.exception.ClientException;
import static testtask.accounts.exception.MicroserviceException.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Feb 2, 2018
 */
public class MksUtil {

    /**
     *
     * @param <T>
     * @param body
     * @param clazz
     * @return
     */
    public static <T extends Object> T convertBodyValue(Object body, Class<T> clazz) {
        try {
            T obj = new ObjectMapper().convertValue(body, clazz);
            return obj;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     *
     * @param <T>
     * @param body
     * @param clazz
     * @return
     */
    public static <T extends Object> T readBodyValue(InputStream body, Class<T> clazz) {
        try {
            T obj = new ObjectMapper().readValue(body, clazz);
            return obj;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 
     * @param <T>
     * @param body
     * @param clazz
     * @return
     * @throws ClientException 
     */
    public static <T extends Object> T parseResponse(Object body, Class<T> clazz) throws ClientException {

        if (body == null) {
            throw new ClientException(ErrorTypes.mks_response_null);
        }

        T obj = convertBodyValue(body, clazz);
        if (obj != null) {
            return obj;
        } else {
            throw new ClientException(ErrorTypes.mks_response_unknown, "Can't parse response " + body);
        }

    }

    /**
     *
     * @param bodyErrorDto
     * @param url
     * @return
     * @throws ClientException
     */
    public static ClientException createClientExceptionFromResponseError(Object bodyErrorDto, String url) throws ClientException {
        String message = String.format("Mks Accounts Error, url: %s.", url);

        if (bodyErrorDto == null) {
            throw new ClientException(ErrorTypes.mks_response_null, message);
        }

        ApiErrorDto errorDto = convertBodyValue(bodyErrorDto, ApiErrorDto.class);
        message += (errorDto != null) ? errorDto.toString() : "Unknown response body";

        return new ClientException(ErrorTypes.bad_mks_request, message);
    }
}
