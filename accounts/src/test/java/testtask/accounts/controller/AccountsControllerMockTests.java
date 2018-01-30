/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.controller;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import testtask.accounts.AccountsApplication;
import static testtask.accounts.TestHelper.*;
import testtask.accounts.exception.AccountExceptionHandler;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;
import testtask.accounts.service.AccountService;
//import org.mockito.runners.MockitoJUnit44Runner;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@RunWith(MockitoJUnitRunner.class)
//@EnableWebMvc
public class AccountsControllerMockTests {

    private static final String URL = "/accounts";

//    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountsController controller;

    private JacksonTester<Account> accountJTester;
    JacksonTester<Account[]> listAccountJTester;
            
    @Before
    public void init() {
        // initialize Jackson Tester
        JacksonTester.initFields(this, AccountsApplication.serializingObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AccountExceptionHandler()) // Init Exception Handler
                .setMessageConverters(new MappingJackson2HttpMessageConverter(AccountsApplication.serializingObjectMapper()))// init custom serializator
                .build();
    }
//    @Test
//    public void blob() {
//
//        assertThat(service).isNotNull();
//        assertThat(mockMvc).isNotNull();
//        assertThat(accountJacksonTester).isNotNull();
//        assertThat(true).isTrue();
//    }
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
        Account[] accounts = listAccountJTester.parseObject(response.getContentAsString());
        assertThat(accounts).isNotNull().isNotEmpty();
        assertThat(accounts).containsExactlyInAnyOrder(account1, account2);
        
    }
    
    @Test
    public void getBadReqyestErrorThenGetAccountByNullClientId() throws Exception{
        
        // then
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/ClientId/"))
                .andDo(print())
                .andReturn().getResponse();
        
        // when
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
