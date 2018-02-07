package testtask.accounts.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import testtask.accounts.AccountsApplication;
import testtask.accounts.exception.AccountException;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;
import testtask.accounts.service.AccountService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Alex Volobuev on 26.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@WithMockUser
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<ApiErrorDto> jacksonErrorTester;

    private Account account;

    @Autowired
    private AccountService accountService;

    @Before
    public void init() {
        // initialize jacksonTester
        JacksonTester.initFields(this, AccountsApplication.serializingObjectMapper());
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        initAccountSalary(100.20, Currency.RUB, 777L, "Зарплатный");
    }

    @After
    public void cleanup() {
        cleanupAccountSalary();
    }

    private void initAccountSalary(double balance, Currency currency, long clientId, String name) {
        account = new Account();
        account.setBalance(new BigDecimal(balance));
        account.setCurrency(currency);
        account.setClientId(clientId);
        account.setName(name);
        account = accountService.create(account);
    }

    private void cleanupAccountSalary () {
        accountService.delete(account);
    }


    @Test
    public void getErrorDtoWhenRequestNotExistingAccount() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/accounts/123"))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        AccountException exception = new AccountException(AccountException.ErrorTypes.not_found,
                "Account with id: " +
                "123 not found");
        ApiErrorDto errorDto = new ApiErrorDto(exception);
        assertThat(response.getContentAsString()).isEqualTo(jacksonErrorTester.write(errorDto).getJson());
    }

    @Test
    public void update() throws Exception {

        mockMvc.perform(put("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("  {\n" +
                        "   \"clientId\": 1,\n" +
                        "   \"name\": \"валютный\",\n" +
                        "   \"balance\": 121 ,\n" +
                        "   \"currency\": \"USD\"\n" +
                        "   }"))
                .andExpect(status().isOk());

    }

}
