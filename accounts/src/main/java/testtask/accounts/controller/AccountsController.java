package testtask.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("")
    public Iterable getAll() {
        return accountService.getAll();
    }

    @PostMapping()
    public ResponseEntity<Account> create(@RequestBody Account account) {
        account = accountService.create(account);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable("id") Long id) {
        Account account = accountService.get(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Account account) {
        account.setId(id);
        accountService.update(account);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id) {
        accountService.delete(accountService.get(id));
        return new ResponseEntity(HttpStatus.OK);
    }

}
