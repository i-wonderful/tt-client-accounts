package testtask.accounts.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static testtask.accounts.TestHelper.expBadMksRequestMatcher;

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
        mockRequestExchange(new ResponseEntity(null, HttpStatus.OK));

        // then  
        accountMksService.deleteAccountsByClientId(1L);
    }

    @Test
    public void throwExceptionThenDeleteAccountsAndGetErrorResponse() {
        // expect 
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");

        // given
        ApiErrorDto errorApi = createErrorDto();
        ResponseEntity<ApiErrorDto> responseWithError = new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
        mockRequestExchange(responseWithError);

        // then
        accountMksService.deleteAccountsByClientId(1L);

    }

    @Test
    public void findAccountsByClientIdWithOkResponse() {
        // given
        final Account account = createAccount();
        mockRequestGetForEntity(new ResponseEntity(new Account[]{account}, HttpStatus.OK));

        // then
        List<Account> accounts = accountMksService.findAccountsByClientId(1L);

        // when
        assertThat(accounts).hasSize(1);
        assertThat(accounts).containsExactly(account);
    }

    @Test
    public void throwExceptionThenFindAccountsAndGetErrorResponce() {
        // expect
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");
        
        // given
        final ApiErrorDto errorDto = createErrorDto();
        mockRequestGetForEntity(new ResponseEntity(errorDto, HttpStatus.INTERNAL_SERVER_ERROR));

        // then
        accountMksService.findAccountsByClientId(1L);

    }

    private void mockRequestExchange(final ResponseEntity response) {
        BDDMockito.given(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                any(Class.class)))
                .willReturn(response);
    }

    private void mockRequestGetForEntity(final ResponseEntity response) {
        BDDMockito.given(restTemplate.getForEntity(anyString(), any(Class.class))).willReturn(response);
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(3451253.23));
        account.setClientId(78L);
        account.setCurrency(Currency.RUB);
        account.setName("Some Account");
        return account;
    }

    private ApiErrorDto createErrorDto() {
        ApiErrorDto errorApi = new ApiErrorDto();
        errorApi.setErrType("SomeTypeError");
        errorApi.setMessage("Something is bad");
        errorApi.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return errorApi;
    }

}
