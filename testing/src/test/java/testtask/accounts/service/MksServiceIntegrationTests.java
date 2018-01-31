package testtask.accounts.service;

import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import testtask.accounts.ClientsApplication;
import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;
import testtask.accounts.model.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

//import static org.hamcrest.MatcherAssert.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 28, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {ClientsApplication.class/*, AccountsApplication.class*/})
@TestPropertySource(locations = "classpath:application.properties")
@WithMockUser
public class MksServiceIntegrationTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /* Test Data */
    private Client clientWithAccounts;
    private List<Account> accounts;
    private Client clientWithoutAccounts;

    @Before
    public void init() {

        ClientEntity clientWithAccountsEntity = new ClientEntity();
        clientWithAccountsEntity.setFirstName("James");
        clientWithAccountsEntity.setLastName("Cameron");
        clientWithAccountsEntity = clientRepository.save(clientWithAccountsEntity);

        ClientEntity clientWithoutAccountsEntity = new ClientEntity();
        clientWithoutAccountsEntity.setFirstName("Robert");
        clientWithoutAccountsEntity.setLastName("Zemeckis");
        clientWithoutAccountsEntity.setMiddleName("Lee");
        clientWithoutAccountsEntity = clientRepository.save(clientWithoutAccountsEntity);

        AccountEntity account1 = new AccountEntity();
        account1.setName("Main Acc");
        account1.setCurrency(Currency.USD);
        account1.setBalance(new BigDecimal(543757));
        account1.setClientId(clientWithAccountsEntity.getId());

        AccountEntity account2 = new AccountEntity();
        account2.setName("Film Budget");
        account2.setCurrency(Currency.USD);
        account2.setBalance(new BigDecimal(754555.95));
        account2.setClientId(clientWithAccountsEntity.getId());

        List<AccountEntity> accountsEntities = new ArrayList<>();
        accountRepository.save(Arrays.asList(account1 ,account2)).forEach(accountsEntities::add);

        clientWithAccounts = ClientConverter.entityToModel(clientWithAccountsEntity);
        accounts = AccountConvertor.entityListToModels(accountsEntities);
        clientWithoutAccounts = ClientConverter.entityToModel(clientWithoutAccountsEntity);
    }

    @Test
    public void findClientWithAccounts() {
        Client clientFind = clientService.findWithAccounts(clientWithAccounts.getId());

        assertNotNull(clientFind);
        assertNotNull(clientFind.getAccounts());

        // check client
        assertEquals(clientWithAccounts.getFirstName(), clientFind.getFirstName());
        assertEquals(clientWithAccounts.getLastName(), clientFind.getLastName());

        // check accounts
        assertThat(clientFind.getAccounts(), containsInAnyOrder(accounts.toArray()));
    }

    @Test
    public void throwNotFoundExceptionWhenClientNotExist() {
        final long notExistedId = 535434534L;

        thrown.expect(expNotFoundMatcher(notExistedId));
        clientService.findWithAccounts(notExistedId);
    }

    @Test
    public void findClientWitoutAccounts() {
        Client clientFind = clientService.findWithAccounts(clientWithoutAccounts.getId());

        assertNotNull(clientFind);
        assertNotNull(clientFind.getAccounts());

        assertTrue(clientFind.getAccounts().isEmpty());
    }

    @Test
    public void deleteClientWithAccounts(){
        
        final long clientId = clientWithAccounts.getId();
        clientService.delete(clientId);
        
        assertThat("Client is not delete by Id", clientRepository.exists(clientId), is(false));
        assertThat("Accounts was not deleted by clientId", accountRepository.findByClientId(clientId), hasSize(0));
    }
    
    /**
     * Create Matcher for not found exception with standard message.
     *
     * @param notExistedId
     * @return
     */
    public static CustomMatcher<ClientException> expNotFoundMatcher(final long notExistedId) {
        return new CustomMatcher<ClientException>("Check ClientException for not found client.") {
            @Override
            public boolean matches(Object o) {

                if (o instanceof ClientException == false) {
                    return false;
                }

                ClientException ex = (ClientException) o;
                return ex.getClientId().equals(notExistedId)
                        && ex.getType().equals(MicroserviceException.ErrorTypes.not_found)
                        && ex.getInfo().equals(ClientException.getStandartInfo(MicroserviceException.ErrorTypes.not_found, notExistedId));
            }
        };
    }
}
