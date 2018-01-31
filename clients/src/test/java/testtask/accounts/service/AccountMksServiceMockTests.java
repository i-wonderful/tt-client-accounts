package testtask.accounts.service;

import static org.assertj.core.api.Assertions.*;
import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static testtask.accounts.TestHelper.*;
import testtask.accounts.exception.ApiErrorDto;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountMksServiceMockTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountMksService accountMksService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void deleteWithOkResponce() {

        // given
        mockRequestWith(new ResponseEntity(null, HttpStatus.OK));

        // then  
        int result = accountMksService.deleteAccountsByClientId(1L);

        // when
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void throwExceptionThenErrorResponse() {

        // given
        ApiErrorDto errorApi = new ApiErrorDto();
        errorApi.setErrType("Some Error");
        errorApi.setMessage("Something is bad");
        errorApi.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseEntity<ApiErrorDto> responseWithError = new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
        mockRequestWith(responseWithError);

        // when
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage(StringContains.containsString("Error in mks accounts."));

        // then
         accountMksService.deleteAccountsByClientId(1L);

    }

    private void mockRequestWith(final ResponseEntity response) {
        BDDMockito.given(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                any(Class.class)))
                .willReturn(response);
    }

}
