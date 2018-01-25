package testtask.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.dao.*;

/**
 *
 */
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    // тут будет тоже сервис
    final AccountRepository accountRepository;

    public AccountsController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
       
    
    @GetMapping(value = "/go")
    @ResponseBody
    public String go() {
        return "Go go go";
    }
    
    @GetMapping(value = "/all")
    public Iterable getAll(){
        return accountRepository.findAll();
    }
}
