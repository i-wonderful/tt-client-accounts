package testtask.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;
import testtask.accounts.service.AccountService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Alex Volobuev on 26.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Account> jacksonTester;

    private Account account;

    @Autowired
    private AccountService accountService;

    @Before
    public void init() {
        // initialize jacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        initAccountSalary();
    }

    @After
    public void cleanup() {
        cleanupAccountSalary();
    }

    private void initAccountSalary () {
        account = new Account();
        account.setBalance(new BigDecimal(100.20));
        account.setCurrency(Currency.RUB);
        account.setClientId(777L);
        account.setName("Зарплатный");
        account = accountService.create(account);
    }

    private void cleanupAccountSalary () {
        accountService.delete(account);
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void create() throws Exception {
    }

    @Test
    @Ignore
    public void getById() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/accounts/" + account.getId()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(jacksonTester.write(account).getJson());

    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

}