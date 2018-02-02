package testtask.accounts.exception;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import testtask.accounts.util.MksUtil;

/**
 * Handler exceptions from other microservises.
 * 
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Feb 2, 2018
 */
public class ResponseMksErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ApiErrorDto apiErrorDto = MksUtil.readBodyValue(response.getBody(), ApiErrorDto.class);
        if (apiErrorDto != null) { // known error
            throw new ClientException(MicroserviceException.ErrorTypes.bad_mks_request, apiErrorDto.toString());
        } else { // unknown error
            super.handleError(response);
        }
    }

}
