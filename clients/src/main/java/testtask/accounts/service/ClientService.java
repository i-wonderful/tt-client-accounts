package testtask.accounts.service;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.exception.ClientException;
import testtask.accounts.model.Account;
import testtask.accounts.model.Client;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@SuppressWarnings("JavaDoc")
@Service
@Slf4j
public class ClientService {

    private final ClientRepository repository;

    private final AccountMksService accountsMksService;

    @Autowired
    public ClientService(AccountMksService accMksService, ClientRepository repository) {
        this.repository = repository;
        this.accountsMksService = accMksService;
    }

    /**
     * Find Client By id
     *
     * @param id
     * @return
     */
    public Client findOne(Long id) {
        ClientEntity entity = repository.findOne(id);
        if (entity == null) {
            throw new ClientException(id, ErrorTypes.not_found);
        }
        return ClientConverter.toModel(entity);
    }

    /**
     * Find Client with accounts.
     *
     * @param id
     * @return
     */
    public Client findWithAccounts(Long id) {

        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Client Id must be not null.");
        }

        Client client = findOne(id);
        List<Account> accounts = accountsMksService.findAccountsByClientId(id);
        client.setAccounts(accounts);
        return client;
    }

    /**
     * Create new Client with accounts.
     *
     * @param client
     * @return
     */
    @Transactional
    public Client create(Client client) {
        if (client == null) {
            throw new ClientException(ErrorTypes.validation, "Null client not allowed");
        }

        if (client.getId() != null) {
            throw new ClientException(client.getId(), ErrorTypes.validation, "Can't create client with predefined id");
        }

        // save client
        Single<Client> jobClientSave = Single.fromCallable(() -> {
            ClientEntity entity = repository.save(ClientConverter.toEntity(client));
            Client clientSaved = ClientConverter.toModel(entity);
            return clientSaved;
        });
        
        // save accounts
        Single<List<Account>> jobAccountSave = jobClientSave.flatMap((Client clientSaved) -> {
            List<Account> accountsSavesd = accountsMksService.createAccounts(clientSaved.getId(), client.getAccounts());
            return Single.just(accountsSavesd);
        });
        
//        job1 = job1.subscribeOn(Schedulers.io());
//        jobAccountSave = jobAccountSave.subscribeOn(Schedulers.io());
        
        Client clientResult = Single.zip(jobClientSave, jobAccountSave, (Client clientSaved, List<Account> accountsSaved) -> {
            clientSaved.setAccounts(accountsSaved);
            return clientSaved;
        }).blockingGet();

        return clientResult;
    }

    /**
     * Update Client with accounts.
     *
     * @param id
     * @param client
     * @return
     */
    @Transactional
    public Client update(final Long id, final Client client) {

        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Can't update, id must be not null.");
        }

        if (!repository.exists(id)) {
            throw new ClientException(client.getId(), ErrorTypes.not_found);
        }

        // action update accounts
        Single<List<Account>> jobAccountsMks = Single.fromCallable(() -> accountsMksService.updateAccounts(id, client.getAccounts()));

        // action update client
        Single<Client> jobClient = Single.fromCallable(() -> {
            client.setId(id);
            return ClientConverter.toModel(repository.save(ClientConverter.toEntity(client)));
        });

        // multithreading
        jobAccountsMks = jobAccountsMks.subscribeOn(Schedulers.io());
        //jobClient = jobClient.subscribeOn(Schedulers.io());

        // wait all results
        Client clientResult = Single.zip(jobAccountsMks, jobClient, (accounts, clientUpdated) -> {
            clientUpdated.setAccounts(accounts);
            return clientUpdated;
        }).blockingGet();

        return clientResult;
    }

    /**
     * Delete Client with Accounts.
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) throws RuntimeException {
        if (id == null) {
            throw new ClientException(ErrorTypes.validation, "Can't delete, id must be not null");
        }

        if (!repository.exists(id)) {
            throw new ClientException(id, ErrorTypes.not_found);
        }

        // action 1
        Completable jobAccountsMks = Completable.fromAction(() -> {
            log.info(">>> ActionOne, deleteAccountsByClientId >> Thread: " + Thread.currentThread().getName());
            accountsMksService.deleteAccountsByClientId(id);
        });

        // action 2
        Completable jobMain = Completable.fromAction(() -> {
            log.info(">>> ActionTwo >> Thread: " + Thread.currentThread().getName());
            repository.delete(id);
        });

        // multithreading
        jobAccountsMks = jobAccountsMks.subscribeOn(Schedulers.io());

        // wait both actions
        jobMain.mergeWith(jobAccountsMks).blockingAwait();

    }
}
