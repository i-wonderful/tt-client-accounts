package testtask.accounts.dao;

import testtask.accounts.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Volobuev on 25.01.2018.
 */
public class AccountConvertor {
    public static Account toModel (AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setBalance(accountEntity.getBalance());
        account.setClientId(accountEntity.getClientId());
        account.setCurrency(accountEntity.getCurrency());
        account.setName(accountEntity.getName());
        return account;
    }

    public static AccountEntity toEntity (Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(account.getId());
        accountEntity.setBalance(account.getBalance());
        accountEntity.setClientId(account.getClientId());
        accountEntity.setCurrency(account.getCurrency());
        accountEntity.setName(account.getName());
        return accountEntity;
    }

    public static List<Account> toModels (Iterable<AccountEntity> accountEntityList) {
        List<Account> accountList = new ArrayList<>();
        accountEntityList.forEach(accountEntity ->
                accountList.add(AccountConvertor.toModel(accountEntity)));
        return accountList;
    }

    public static List<AccountEntity> toEntities (Iterable<Account> accountList) {
        List<AccountEntity> entityList = new ArrayList<>();
        accountList.forEach(account ->
                entityList.add(AccountConvertor.toEntity(account)));
        return entityList;
    }

}
