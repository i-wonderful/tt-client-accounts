package testtask.accounts.dao;

import testtask.accounts.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Volobuev on 25.01.2018.
 */
public class AccountConvertor {
    public static Account entityToModel (AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setBalance(accountEntity.getBalance());
        account.setClientId(accountEntity.getClientId());
        account.setCurrency(accountEntity.getCurrency());
        account.setName(accountEntity.getName());
        return account;
    }

    public static AccountEntity modelToEntity (Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(account.getId());
        accountEntity.setBalance(account.getBalance());
        accountEntity.setClientId(account.getClientId());
        accountEntity.setCurrency(account.getCurrency());
        accountEntity.setName(account.getName());
        return accountEntity;
    }

    public static List<Account> entityListToModels (Iterable<AccountEntity> accountEntityList) {
        List<Account> accountList = new ArrayList<>();
        accountEntityList.forEach(accountEntity ->
                accountList.add(AccountConvertor.entityToModel(accountEntity)));
        return accountList;
    }

}
