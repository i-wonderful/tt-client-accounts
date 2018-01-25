package testtask.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.model.Account;
import testtask.accounts.service.AccountService;


@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public Iterable getAll() {
        return accountService.getAll();
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {

        account = accountService.create(account);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }
}
