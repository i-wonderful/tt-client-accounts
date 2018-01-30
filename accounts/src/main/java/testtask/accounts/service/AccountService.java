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
import testtask.accounts.exception.MicroserviceException;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidations validations;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidations validations) {
        this.accountRepository = accountRepository;
        this.validations = validations;
    }

    public Account create(Account account) {
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        accountEntity = accountRepository.save(accountEntity);
        account = AccountConvertor.entityToModel(accountEntity);
        log.info("Create account: " + account);
        return account;
    }

    public List<Account> create(List<Account> accounts) {
        if (accounts == null) {
            throw new AccountException(MicroserviceException.ErrorTypes.validation);
        }
        
        List<Account> saved = AccountConvertor.entityListToModels(accountRepository.save(AccountConvertor.modelsListToEntities(accounts)));
        log.info("Create List Accounts: " + saved.toString());
        return saved;
    }

    public Account get(Long id) {
        AccountEntity accountEntity = accountRepository.findOne(id);
        if (accountEntity == null) {
            throw new AccountException(AccountException.ErrorTypes.not_found,
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
        accountRepository.save(accountEntity);
    }

    public void delete(Account account) {
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        log.info("Delete account: " + account);
        accountRepository.delete(accountEntity);
    }

    public List<Account> getAll() {
        Iterable<AccountEntity> accountEntities = accountRepository.findAll();
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        log.info("Get all accounts: " + accounts);
        return accounts;
    }

    public List<Account> findByClientId(Long clientId) {
        Iterable<AccountEntity> accountEntities = accountRepository.findByClientId(clientId);
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        log.info("Find all accounts of client with ID {} : {}", clientId, accounts);
        return accounts;
    }

    public void updateAllAccountsOfClient(Iterable<Account> accounts, Long clientId) {
        //TODO delete all before bulk update
        validations.allAccountsHasClientId(accounts, clientId);
        Iterable<AccountEntity> accountEntities = AccountConvertor.modelsListToEntities(accounts);
        accountRepository.save(accountEntities);
        log.info("Bulk account update of client with ID {} : {}", clientId, accounts);
    }

    public void deleteAllAccountsOfClient(Long clientId) {
        Iterable<Account> accounts = findByClientId(clientId);
        Iterable<AccountEntity> accountEntities = AccountConvertor.modelsListToEntities(accounts);
        accountRepository.delete(accountEntities);
        log.info("Bulk account delete of client with ID {} : {}", clientId, accounts);
    }

}
