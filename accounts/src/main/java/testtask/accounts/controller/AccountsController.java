package testtask.accounts.controller;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.dao.*;
import testtask.accounts.model.Account;

/**
 *
 */
@RestController
@RequestMapping("/account")
public class AccountsController {

    // тут будет тоже сервис
    final AccountRepository accountRepository;

    public AccountsController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }

    @GetMapping(value = "/go")
    @ResponseBody
    public String go() {
        AsyncSubject<Integer> subject = AsyncSubject.create();

        Disposable subscribe = subject.subscribe(i -> {
            System.out.println(i);
        });
        subject.onNext(12);
        subject.onNext(45);
        subject.onComplete();
        return "Go go go";
    }

    @GetMapping(value = "/all")
    public Iterable getAll() {
        // Todo for tests
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(new BigDecimal(12321)));
        accounts.add(new Account(new BigDecimal(9987)));
        
        return accounts;
        //return accountRepository.findAll();
    }
}
