package testtask.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import testtask.accounts.exception.AccountException;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Account;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Alex Volobuev on 28.01.2018.
 */
@Component
public class AccountValidations {

    private final Validator validator;

    @Autowired
    public AccountValidations(Validator validator) {
        this.validator = validator;
    }

    public void validateAccount(Account account) {

        validateNotNull(account,"Null Account not allowed");

        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);
        if (!constraintViolations.isEmpty()) {
            //TODO тут по-хорошему можно бы все ошибки собрать сразу, но это тест.задание, так что будет первая
            @SuppressWarnings("ConstantConditions")
            ConstraintViolation<Account> error = constraintViolations.stream().findFirst().get();
            String info = error.getPropertyPath() + " = " + error.getInvalidValue() + ". " + error.getMessage();
            throw new AccountException(MicroserviceException.ErrorTypes.validation, info);
        }
    }

    public void accountHasClientId(Account account, Long clientId) throws AccountException {
        if (!Objects.equals(account.getClientId(), clientId)) {
            throw new AccountException(MicroserviceException.ErrorTypes.validation, "Account " + account.getId() + " " +
                    "doesn't belong to client with id: " + clientId);
        }
    }

    public void allAccountsHasClientId(List<Account> accountList, Long clientId) throws AccountException {
        accountList.forEach(account -> accountHasClientId(account, clientId));
    }

    public void createValidations (Account account) {
        validateAccount(account);
        if (account.getId() != null) {
            throw new AccountException(MicroserviceException.ErrorTypes.validation,
                    "Can't create Account with predefined id: " + account.getId());
        }
    }

    public void createValidations (List<Account> accounts) {
        validateNotNull(accounts,"Can't create null accounts");
        accounts.forEach(this::createValidations);
    }

    public <T> void validateNotNull (T id, String errorMessage) {
        if (id == null) {
            throw new AccountException(MicroserviceException.ErrorTypes.null_argument,errorMessage);
        }
    }


}
