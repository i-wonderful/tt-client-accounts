package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.accounts.exception.AccountException;
import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.model.Account;

import java.util.List;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(Account account) {
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        accountEntity = accountRepository.save(accountEntity);
        account = AccountConvertor.entityToModel(accountEntity);
        return account;
    }

    //TODO transactions if necessary
    public Account get(Long id) {
        AccountEntity accountEntity = accountRepository.findOne(id);
        if (accountEntity == null) {
            throw new AccountException(AccountException.ErrorTypes.not_found ,"Account with id: " + id + " not found");
        }
        Account account = AccountConvertor.entityToModel(accountEntity);
        //TODO logging
        return account;
    }

    public void update(Account account) {
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        accountRepository.save(accountEntity);
    }

    public void delete(Account account) {
        AccountEntity accountEntity = AccountConvertor.modelToEntity(account);
        accountRepository.delete(accountEntity);
    }

    public List<Account> getAll() {
        Iterable<AccountEntity> accountEntities = accountRepository.findAll();
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        return accounts;
    }

    public List<Account> findByClientId(Long clientId) {
        Iterable<AccountEntity> accountEntities = accountRepository.findByClientId(clientId);
        List<Account> accounts = AccountConvertor.entityListToModels(accountEntities);
        return accounts;
    }

}
