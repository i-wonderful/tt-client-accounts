package testtask.accounts.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import testtask.accounts.AccountsApplication;
import testtask.accounts.exception.AccountExceptionHandler;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;
import testtask.accounts.service.AccountService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static testtask.accounts.TestHelper.createAccountModel;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountsControllerMockTests {

    private static final String URL = "/accounts";

    private MockMvc mockMvc;

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountsController controller;

    private JacksonTester<Account> accountJTester;
    private JacksonTester<List<Account>> listAccountJTester;

    @Before
    public void init() {
        // initialize Jackson Tester
        JacksonTester.initFields(this, AccountsApplication.serializingObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AccountExceptionHandler()) // Init Exception Handler
                .setMessageConverters(new MappingJackson2HttpMessageConverter(AccountsApplication.serializingObjectMapper()))// init custom serializator
                .build();
    }

    @Test
    public void findAccountById() throws Exception {
        // given
        long accountId = 1L;
        final Account account = createAccountModel(accountId, 535234.64, Currency.RUB, 55L, "Deposit Rub");
        BDDMockito.given(service.get(1L)).willReturn(account);

        // then
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/" + accountId))
                .andDo(print())
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Account findAccount = accountJTester.parseObject(response.getContentAsString());
        assertThat(findAccount).isEqualTo(account);
    }

    @Test
    public void findAccountsByClientId() throws Exception {
        // given
        final long clientId = 1L;
        final Account account1 = createAccountModel(111L, 3456.34, Currency.USD, clientId, "Account Main");
        final Account account2 = createAccountModel(222L, 124234.6, Currency.RUB, clientId, "Deposit One");
        BDDMockito.given(service.findByClientId(clientId)).willReturn(Arrays.asList(account1, account2));

        // then
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/ClientId/" + clientId))
                .andDo(print())
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        
        List<Account> accounts = listAccountJTester.parseObject(response.getContentAsString());
        assertThat(accounts).isNotNull().isNotEmpty();
        assertThat(accounts).containsExactlyInAnyOrder(account1, account2);

    }

    @Test
    public void getBadRequestErrorThenGetAccountByNullClientId() throws Exception {

        // then
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/ClientId/"))
                .andDo(print())
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteAccountByAccountId() throws Exception {

        // given
        final long accountId = 123L;

        // then
        MockHttpServletResponse response = mockMvc.perform(delete(URL + "/" + accountId))
                .andDo(print())
                .andReturn().getResponse();
        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void deleteAccountsByClientId() throws Exception {
        final long clientId = 558L;

        // then
        MockHttpServletResponse response = mockMvc.perform(delete(URL + "/clientId/" + clientId))
                .andDo(print())
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void createAccountsList() throws Exception {
        // given
        final List<Account> accounts = Arrays.asList(createAccountModel(34545.91, Currency.RUB, 67L, "SomeAcc"));
        BDDMockito.given(service.create(accounts)).willReturn(accounts);

        // then
        MockHttpServletResponse response = mockMvc.perform(post(URL + "/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(listAccountJTester.write(accounts).getJson()))
                .andDo(print())
                .andReturn().getResponse();

        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<Account> accountsSaved = listAccountJTester.parseObject(response.getContentAsString());
        assertThat(accountsSaved).hasSameSizeAs(accounts);
    }

}
