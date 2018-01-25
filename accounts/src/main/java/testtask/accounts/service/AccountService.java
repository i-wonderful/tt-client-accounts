package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.accounts.dao.AccountConvertor;
import testtask.accounts.dao.AccountEntity;
import testtask.accounts.dao.AccountRepository;
import testtask.accounts.model.Account;

import java.util.ArrayList;
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

    public Account create (Account account) {
        account = AccountConvertor.entityToModel(
                accountRepository.save(
                        AccountConvertor.modelToEntity(account)
                )
        );
        return account;
    }

//    public Account read (Long id) {
//        return account;
//    }

//    public void update (Account account) {
//        return;
//    }

//    public void delete (Account account) {
//        return;
//    }

        public List<Account> getAll () {
        List<Account> allAccounts = new ArrayList<>();
        Iterable<AccountEntity> allAccountEntities = accountRepository.findAll();
        allAccountEntities.forEach(accountEntity -> allAccounts.add(AccountConvertor.entityToModel(accountEntity)));
        return allAccounts;
    }

}
