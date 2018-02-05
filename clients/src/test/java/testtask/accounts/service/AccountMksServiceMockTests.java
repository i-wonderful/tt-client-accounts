package testtask.accounts.service;

import com.google.common.collect.HashBiMap;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.model.Account;
import testtask.accounts.model.Currency;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Assert;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static testtask.accounts.TestHelper.expBadMksRequestMatcher;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 30, 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountMksServiceMockTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountMksService accountMksService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    String result;
//        String rez = "";

    @Test
    public void testRX() {

        Observable<String> observable = Observable.just("Eeee"); // provides data
        observable.subscribe(s -> result = s);
        System.out.println(">>> " + result);
        Assert.assertTrue(result.equals("Eeee"));
    }

    @Test
    public void testRx2() {
        Maybe<String> maybe = Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> emitter) throws Exception {
                Thread thread = new Thread( () -> {
//                thread.
//                    @Override
//                    public void run() {
                        System.out.println(">>> Observable On Subscribe! Thread: " + Thread.currentThread().getName());
                        emitter.onSuccess("New Hey");
//                    }
                });
                thread.run();

//                emitter.onError(new RuntimeException());
//                emitter.onComplete();
            }
        });

        Maybe<String> maybeCashed = maybe.cache();

        // Subscribe !!! On Added Data
        Disposable disposable = maybeCashed.subscribe(new Consumer<String>() {
            @Override
            public void accept(String t) throws Exception {

                System.out.println(">>> Subscriber1 >> Thread:" + Thread.currentThread().getName() + ">> Value:" + t);
            }
        });

        // unsubscribe subscription
        disposable.dispose();

        // Subscribe second subscription!!! 
        maybeCashed.subscribe(val -> {
            System.out.println(">>> Subscriber 2 >> Value: " + val + " >> Thread " + Thread.currentThread().getName());
        }, err -> {
            System.out.println(">>> Subscriber 2 >> Error: " + err);
        });

        // Third subscription
        MaybeObserver<String> subscribeWith = maybeCashed.subscribeWith(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(">> Subscribe With: OnSubscribe");
            }

            @Override
            public void onSuccess(String t) {
                System.out.println(">> Subscribe With: On Success");
            }

            @Override
            public void onError(Throwable e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onComplete() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

//        CompositeDisposable cd = new CompositeDisposable();
//        cd.
    }
    
    @Test
    public void javaRXZipWith(){
    
       Maybe<List<String>> maybe = Maybe.create(new MaybeOnSubscribe<List<String>>() {
           @Override
           public void subscribe(MaybeEmitter<List<String>> emitter) throws Exception {
               List<String> stringData = Arrays.asList("one", "two");
               
               emitter.onSuccess(stringData);
//               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           }
       });
       
       Maybe<Integer> anotherObservable = Maybe.just(123321);
        
       maybe.subscribe(new Consumer<List<String>>() {
           @Override
           public void accept(List<String> t) throws Exception {
               System.out.println(">>> " + t.toString());
//               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           }
       });
       
       anotherObservable.zipWith(maybe,new BiFunction<Integer, List<String>, Map>() {
           @Override
           public Map apply(Integer t1, List<String> t2) throws Exception {
               Map<Integer, List> map = new HashMap<>();
               map.put(t1, t2);
               return map;
           }
       } ).subscribe(new Consumer<Map>() {
           @Override
           public void accept(Map t) throws Exception {
               
               System.out.println(">> Map Result " + t);
//               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           }
       });
       
       //Maybe.zip(maybe, maybe, zipper)
    }


    @Test
    public void observableT() {
        Observable s = Observable.just(new Object());
        s.map(val -> val.toString());
        System.out.println("> Th " + Thread.currentThread().getName());

        Observable.create(subscriber -> {
            System.out.println("> " + Thread.currentThread().getName());
            subscriber.onNext(new Object());
            subscriber.onComplete();
        }).subscribeOn(Schedulers.io())// new thread
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    System.out.println(">> " + Thread.currentThread().getName());
                }, error -> {
                });

//        CompositeSubscription sa;
//ObservableObserver.range(1, 2).subscribe(onNext)
    }

    @Test
    public void deleteWithOkResponse() {

        // given
        mockRequestExchange(new ResponseEntity(null, HttpStatus.OK));

        // then  
        accountMksService.deleteAccountsByClientId(1L);
    }

    @Test
    public void throwExceptionThenDeleteAccountsAndGetErrorResponse() {
        // expect 
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");

        // given
        ApiErrorDto errorApi = createErrorDto();
        ResponseEntity<ApiErrorDto> responseWithError = new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
        mockRequestExchange(responseWithError);

        // then
        accountMksService.deleteAccountsByClientId(1L);

    }

    @Test
    public void findAccountsByClientIdWithOkResponse() {
        // given
        final Account account = createAccount();
        mockRequestGetForEntity(new ResponseEntity(new Account[]{account}, HttpStatus.OK));

        // then
        List<Account> accounts = accountMksService.findAccountsByClientId(1L);

        // when
        assertThat(accounts).hasSize(1);
        assertThat(accounts).containsExactly(account);
    }

    @Test
    public void throwExceptionThenFindAccountsAndGetErrorResponse() {
        // expect
        thrown.expect(expBadMksRequestMatcher());
        thrown.expectMessage("Mks Accounts Error, url");

        // given
        final ApiErrorDto errorDto = createErrorDto();
        mockRequestGetForEntity(new ResponseEntity(errorDto, HttpStatus.INTERNAL_SERVER_ERROR));

        // then
        accountMksService.findAccountsByClientId(1L);

    }

    @Test
    public void createAccounts() {

        // given
        final List<Account> accounts = Arrays.asList(createAccount());
        mockRequestExchange(new ResponseEntity(accounts.toArray(), HttpStatus.OK));

        // then
        List<Account> accountsSaved = accountMksService.createAccounts(1L, accounts);

        // when
        assertThat(accountsSaved).isNotNull();
        assertThat(accountsSaved).hasSameSizeAs(accounts);

    }

    @Test
    public void throwExceptionThenCreateAccountsMksReturnError() {

        // expect
        thrown.expect(expBadMksRequestMatcher());

        // given
        mockRequestExchange(new ResponseEntity(createErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR));

        // when
        accountMksService.createAccounts(1L, Arrays.asList(createAccount(), createAccount()));
    }

    private void mockRequestExchange(final ResponseEntity response) {
        BDDMockito.given(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                any(Class.class)))
                .willReturn(response);
    }

    private void mockRequestGetForEntity(final ResponseEntity response) {
        BDDMockito.given(restTemplate.getForEntity(anyString(), any(Class.class))).willReturn(response);
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(3451253.23));
        account.setClientId(78L);
        account.setCurrency(Currency.RUB);
        account.setName("Some Account");
        return account;
    }

    private ApiErrorDto createErrorDto() {
        ApiErrorDto errorApi = new ApiErrorDto();
        errorApi.setErrType("SomeTypeError");
        errorApi.setMessage("Something is bad");
        errorApi.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return errorApi;
    }

}
