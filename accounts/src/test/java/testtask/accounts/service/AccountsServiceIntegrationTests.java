
package testtask.accounts.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

import static testtask.accounts.TestHelper.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"classpath:application.properties"})
public class AccountsServiceIntegrationTests {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Before
    public void init() {

    }



    @Test
    public void canCreateAccount() {
        Account account = new Account();
        account.setName("New Wonderful Account");
        account.setBalance(new BigDecimal(23423));
        account.setCurrency(Currency.USD);
        account.setClientId(55L);

        Account createdAccount = service.create(account);

        // check success saved
        assertThat(createdAccount).isNotNull();

        // check what entity exist in db
        AccountEntity findCreatedAccountEntity = repository.findOne(createdAccount.getId());
        assertThat(findCreatedAccountEntity).isNotNull();

        // check what created entity is good entity
        Account findCreatedAccount = AccountConvertor.entityToModel(findCreatedAccountEntity);
        assertThat(createdAccount).isEqualTo(findCreatedAccount);
    }

    @Test
    public void canCreateAccountsList() {
        Account acc1 = createAccountModel(123.12, Currency.RUB, 849L, "Acc1");
        Account acc2 = createAccountModel(465657.86, Currency.USD, 77L, "Acc2");

        List<Account> createdAccounts = service.create(Arrays.asList(acc1, acc2));

        // check success saved
        assertThat(createdAccounts).isNotNull();
        assertThat(createdAccounts).hasSize(2);

        // check what entities existed id db
        List<Long> ids = createdAccounts.stream().map(Account::getId).collect(Collectors.toList());
        Iterable<AccountEntity> findCreatedAccountsEntities = repository.findAll(ids);
        assertThat(findCreatedAccountsEntities).hasSize(2);

        // check what created accounts is right accounts 
        List<Account> findCreatedAccounts = AccountConvertor.entityListToModels(findCreatedAccountsEntities);
        assertThat(findCreatedAccounts).containsExactlyInAnyOrder(createdAccounts.toArray(new Account[2]));

    }
}
