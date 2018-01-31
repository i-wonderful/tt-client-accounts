package testtask.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.exception.AccountException;
import testtask.accounts.model.Account;

import java.util.List;

import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
@Slf4j
public class AccountService {

    private final AccountRepository repository;
    private final AccountValidations validations;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidations validations) {
        this.repository = accountRepository;
        this.validations = validations;
    }

    /**
     * Create new Account.
     *
     * @param account
     * @return
     */
    public Account create(Account account) {

        validations.createValidations(account);
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        accountEntity = repository.save(accountEntity);
        account = AccountConvertor.entityToModel(accountEntity);
        log.info("Create account: " + account);
        return account;
    }

    /**
     * Create List Accounts.
     *
     * @param accounts
     * @return
     */
    public List<Account> create(List<Account> accounts) {
        validations.createValidations(accounts);
        Iterable<AccountEntity> accountEntities = AccountConvertor.modelsListToEntities(accounts);
        accountEntities = repository.save(accountEntities);
        accounts = AccountConvertor.entityListToModels(accountEntities);
        log.info("Create List Accounts: " + accounts.toString());
        return accounts;
    }

    public Account get(Long id) {
        AccountEntity accountEntity = repository.findOne(id);
        if (accountEntity == null) {
            throw new AccountException(ErrorTypes.not_found,
                    "Account with id: " + id + " not found");
        }
        Account account = AccountConvertor.entityToModel(accountEntity);
        log.info("Get account: " + account);
        return account;
    }

    public void update(Account account) {
        validations.validateAccount(account);
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        log.info("Update account: " + account);
        repository.save(accountEntity);
    }

    /**
     * Delete Account
     *
     * @param account
     */
    public void delete(Account account) {
        validations.validateAccount(account);
        delete(account.getId());
    }

    /**
     * Delete Account
     *
     * @param id
     */
    public void delete(Long id) {
        validations.validateNotNull(id,"Can't delete account with id = null");
        if (!repository.exists(id)) {
            throw new AccountException(ErrorTypes.not_found, id);
        }
        log.info("Delete account: id=" + id);
        repository.delete(id);
    }

    public void delete(List<Account> accounts) {
        validations.validateNotNull(accounts,"can't delete null accounts");
        log.info("Delete list accounts: {} ", accounts);
        repository.delete(AccountConvertor.modelsListToEntities(accounts));
    }

    public List<Account> getAll() {
        Iterable<AccountEntity> accountEntities = repository.findAll();
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        log.info("Get all accounts: " + accounts);
        return accounts;
    }

    public List<Account> findByClientId(Long clientId) {
        validations.validateNotNull(clientId,"client id is null");
        List<AccountEntity> accountEntities = repository.findByClientId(clientId);
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        log.info("Find all accounts of client with ID {} : {}", clientId, accounts);
        return accounts;
    }

    public void updateAllAccountsOfClient(List<Account> accounts, Long clientId) {
        //TODO delete all before bulk update
        validations.allAccountsHasClientId(accounts, clientId);
        List<AccountEntity> accountEntities = AccountConvertor.modelsListToEntities(accounts);
        repository.save(accountEntities);
        log.info("Bulk account update of client with ID {} : {}", clientId, accounts);
    }

    /**
     *
     * @param clientId
     */
    public void deleteAllAccountsOfClient(Long clientId) {
        validations.validateNotNull(clientId,"clientId is null");
        List<AccountEntity> accountEntities = repository.findByClientId(clientId);
        repository.delete(accountEntities);
        log.info("Bulk account delete of client with ID {} : {}", clientId, accountEntities);
    }

}
