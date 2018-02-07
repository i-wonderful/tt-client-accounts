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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static testtask.accounts.TestHelper.*;
import org.springframework.core.ParameterizedTypeReference;
import static org.mockito.Matchers.any;

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
    public void deleteWithOkResponse() {

        // given
        mockRequestExchange(new ResponseEntity(null, HttpStatus.OK));

        // then  
        accountMksService.deleteAccountsByClientId(1L);
    }

    @Test
    public void throwExceptionWhenDeleteAccountsAndGetErrorResponse() {
        // expect 
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");

        // given
        ApiErrorDto errorApi = createErrorDto();
        ResponseEntity<ApiErrorDto> responseWithError = new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
        mockRequestExchange(responseWithError);

        // when
        accountMksService.deleteAccountsByClientId(1L);

    }

    @Test
    public void findAccountsByClientIdWithOkResponse() {
        // given
        final List<Account> accounts = createAccountsNotNullIdList();
        mockRequestExchangeForList(new ResponseEntity(accounts, HttpStatus.OK));

        // then
        List<Account> accountsFind = accountMksService.findAccountsByClientId(1L);

        // when
        assertThat(accountsFind).hasSameSizeAs(accounts);
        assertThat(accountsFind).isEqualTo(accounts);
    }

    @Test
    public void throwExceptionWhenFindAccountsMksReturnError() {
        // expect
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");

        // given
        final ApiErrorDto errorDto = createErrorDto();
        mockRequestExchangeForList(new ResponseEntity(errorDto, HttpStatus.INTERNAL_SERVER_ERROR));

        // when
        accountMksService.findAccountsByClientId(1L);

    }

    @Test
    public void canCreateAccounts() {

        // given
        final List<Account> accounts = Arrays.asList(createAccount());
        mockRequestExchangeForList(new ResponseEntity(accounts, HttpStatus.OK));

        // when
        List<Account> accountsSaved = accountMksService.createAccounts(1L, accounts);

        // then
        assertThat(accountsSaved).isNotNull();
        assertThat(accountsSaved).hasSameSizeAs(accounts);

    }

    @Test
    public void throwExceptionWhenCreateAccountsMksReturnError() {

        // expect
        thrown.expect(expBadMksRequestMatcher());

        // given
        mockRequestExchangeForList(new ResponseEntity(createErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR));

        // when
        accountMksService.createAccounts(1L, createAccountsNotNullIdList());
    }

    @Test
    public void canUpdateAccounts() {
        // given
        final List<Account> accounts = createAccountsNotNullIdList();
        mockRequestExchangeForList(new ResponseEntity(accounts, HttpStatus.OK));

        // when
        List<Account> accountsUpdated = accountMksService.updateAccounts(accounts.get(0).getClientId(), accounts);

        // then
        assertThat(accountsUpdated).isEqualTo(accounts);
    }

    @Test
    public void throwExceptionWhenUpdateAccountsMksReturnError() {
        // expect
        thrown.expect(expBadMksRequestMatcher());

        // given
        mockRequestExchangeForList(new ResponseEntity(createErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR));

        // when
        accountMksService.updateAccounts(1L, createAccountsNullIdsList());
    }

    private void mockRequestExchange(final ResponseEntity response) {
        BDDMockito.given(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                any(Class.class)))
                .willReturn(response);
    }

    private void mockRequestExchangeForList(final ResponseEntity response) {
        BDDMockito.given(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<ParameterizedTypeReference<?>>any()))
                .willReturn(response);
    }

    private void mockRequestGetForEntity(final ResponseEntity response) {
        BDDMockito.given(restTemplate.getForEntity(anyString(), any(Class.class))).willReturn(response);
    }

    private ApiErrorDto createErrorDto() {
        ApiErrorDto errorApi = new ApiErrorDto();
        errorApi.setErrType("SomeTypeError");
        errorApi.setMessage("Something is bad");
        errorApi.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return errorApi;
    }

}
