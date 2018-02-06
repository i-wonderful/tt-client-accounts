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
import testtask.accounts.AccountsApplication;
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
import org.junit.After;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static testtask.accounts.TestHelper.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 28, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {ClientsApplication.class, AccountsApplication.class})
@TestPropertySource(locations = "classpath:application.properties")
@WithMockUser
public class MksServiceIntegrationTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountMksService mksService;

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
        account2.setBalance(new BigDecimal(754555));
        account2.setClientId(clientWithAccountsEntity.getId());

        Iterable<AccountEntity> accountsEntities = accountRepository.save(Arrays.asList(account1, account2));

        clientWithAccounts = ClientConverter.toModel(clientWithAccountsEntity);
        accounts = AccountConvertor.toModels(accountsEntities);
        clientWithoutAccounts = ClientConverter.toModel(clientWithoutAccountsEntity);
    }

    @After
    public void clear() {
        if (clientRepository.exists(clientWithAccounts.getId())) {
            clientRepository.delete(clientWithAccounts.getId());
        }
        if (clientRepository.exists(clientWithoutAccounts.getId())) {
            clientRepository.delete(clientWithoutAccounts.getId());
        }
        accountRepository.delete(AccountConvertor.toEntities(accounts));
    }

    @Test
    public void findClientWithAccounts() {
        Client clientFind = clientService.findWithAccounts(clientWithAccounts.getId());

        assertThat(clientFind).isNotNull();
        assertThat(clientFind.getAccounts()).isNotNull().isNotEmpty();

        // check client
        assertThat(clientWithAccounts).isEqualTo(clientFind);

        // check accounts
        assertThat(clientFind.getAccounts()).isEqualTo(accounts);

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

        assertThat(clientFind).isNotNull();
        assertThat(clientFind.getAccounts()).isNotNull();

        assertThat(clientFind.getAccounts()).isEmpty();
    }

    @Test
    public void deleteClientWithAccounts() {

        final long clientId = clientWithAccounts.getId();
        clientService.delete(clientId);

        assertThat(clientRepository.exists(clientId)).isFalse(); // "Client is not delete by Id"
        assertThat(accountRepository.findByClientId(clientId)).isEmpty();// "Accounts was not deleted by clientId"
    }

    @Test
    public void canCreateClientWithAccounts() {

        Client clientNew = new Client("Guy", "Ritchie");

        Account acc = new Account();
        acc.setBalance(new BigDecimal(32555500));
        acc.setCurrency(Currency.USD);
        acc.setName("New Action Film Budgettt");

        Account acc2 = new Account();
        acc2.setBalance(new BigDecimal(7345525));
        acc2.setCurrency(Currency.RUB);
        acc2.setName("Additional important account453455");

        clientNew.setAccounts(Arrays.asList(acc, acc2));

        Client clientSaved = clientService.create(clientNew);
        assertThat(clientSaved).isNotNull();
        assertThat(clientSaved.getAccounts()).isNotNull();

        assertThat(clientRepository.exists(clientSaved.getId())).isTrue();
        assertThat(clientSaved.getAccounts()).hasSize(2);
        assertThat(accountRepository.exists(clientSaved.getAccounts().get(0).getId())).isTrue();
        assertThat(accountRepository.exists(clientSaved.getAccounts().get(1).getId())).isTrue();
    }

    @Test
    public void throwBadRequestExceptionThenTryToCreateAccountWithId() {
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Can't create Account with predefined id: ");

        Account acc1 = new Account();
        acc1.setBalance(new BigDecimal(534534));
        acc1.setClientId(55L);
        acc1.setCurrency(Currency.USD);
        acc1.setName("TestSameAcc");
        acc1.setId(2434L);

        mksService.createAccounts(1L, Arrays.asList(acc1));
    }

    /**
     * Check that nothing was save then try to create new client with existing
     * accounts and accounts mks return json with error.
     */
    @Test
    public void rollbackTransactionThenTryToSaveClientWithExistedAccount() {

        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Can't create Account with predefined id");

        long countClientsBefore = clientRepository.count();
        long countAccountsBefore = accountRepository.count();

        Client clientNew = new Client("John", "Jocker");
        clientNew.setAccounts(accounts);
        try {
            clientService.create(clientNew);
        } catch (Exception e) {
            long countClientsAfter = clientRepository.count();
            long countAccountsAfter = accountRepository.count();
            // nothing created
            assertThat(countClientsAfter).isEqualTo(countClientsBefore);
            assertThat(countAccountsAfter).isEqualTo(countAccountsBefore);
            throw e;
        }
    }

    @Test
    public void canUpdateClientWithAccounts() {

        final BigDecimal newBalance = new BigDecimal(11112222);
        final String newName = "Richard";
        Account accountChanged = accounts.get(0);
        accountChanged.setBalance(newBalance);
        clientWithAccounts.setAccounts(accounts);
        clientWithAccounts.setFirstName(newName);

        clientService.update(clientWithAccounts.getId(), clientWithAccounts);

        // check
        AccountEntity accountUpdated = accountRepository.findOne(accountChanged.getId());
        ClientEntity clientUpdated = clientRepository.findOne(clientWithAccounts.getId());

        assertThat(clientUpdated.getFirstName()).isEqualTo(newName);
        assertThat(accountUpdated.getBalance().doubleValue()).isEqualTo(newBalance.doubleValue());
    }

    @Test
    public void rollbackTransactionWhenTryUpdateClientWithAccountsNullId() {

        thrown.expect(expBadMksRequestMatcher());

        final String newLastName = "SomeNewLastName";
        accounts.forEach(acc -> acc.setId(null));
        clientWithAccounts.setAccounts(accounts);
        clientWithAccounts.setLastName(newLastName);
        try {
            clientService.update(clientWithAccounts.getId(), clientWithAccounts);
        } catch (Exception e) {

            Client notUpdatedClient = clientService.findOne(clientWithAccounts.getId());
            assertThat(notUpdatedClient.getLastName()).isNotEqualTo(newLastName);

            throw e;
        }

    }
//    public void rollbackTransactionThenTryToDeleteNullAccount(){
//    
//    long countClient = clientRepository.count();
//    
//    //clientWithAccounts.getAccounts().forEach(acc -> { acc.setId(null);});
//    }

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
