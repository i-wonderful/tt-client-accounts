package testtask.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.exception.MicroserviceException;
import testtask.accounts.model.Account;
import testtask.accounts.service.AccountService;

import javax.validation.Valid;


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

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable("id") Long id) {
        Account account = accountService.get(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/ClientId/{id}")
    public Iterable<Account> getByClientId(@PathVariable("id") Long clientId) {
        return accountService.findByClientId(clientId);
    }

    @PostMapping()
    public ResponseEntity<Account> create(@RequestBody Account account) {
        account = accountService.create(account);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Account account, BindingResult
            bindingResult) {

        ResponseEntity result = validateInput(bindingResult);

        if (result == null) {
            account.setId(id);
            accountService.update(account);
            result = new ResponseEntity(HttpStatus.OK);
        }

        return result;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        accountService.delete(accountService.get(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    private ResponseEntity<ApiErrorDto> validateInput(BindingResult bindingResult) {

        ResponseEntity<ApiErrorDto> errorResult = null;

        if(bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().get(0);
            String cause = "field: " + objectError.getObjectName() + ", message: " + objectError.getDefaultMessage();
            ApiErrorDto dto = new ApiErrorDto(MicroserviceException.ErrorTypes.validation, cause);
            errorResult = new ResponseEntity<>(dto,dto.getStatus());
        }
        return errorResult;
    }

}
