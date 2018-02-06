package testtask.accounts.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import testtask.accounts.ClientsApplication;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Client;

import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import static testtask.accounts.ClientTestHelper.expNotFoundMatcher;
import static testtask.accounts.TestHelper.*;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 26, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class ClientServiceIntegrationTests {

    @MockBean
    private AccountMksService accountMksService;

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /* Test Data */
    private Client clientData;

    @Before
    public void init() {
        ClientEntity entity = new ClientEntity();
        entity.setFirstName("Matthew");
        entity.setLastName("Murdock");
        entity.setMiddleName("Michael");
        entity = repository.save(entity);
        clientData = ClientConverter.toModel(entity);
    }

    @After
    public void clear() {
        repository.deleteAll();
    }

    @Test
    public void testFindFromDb() {

        ClientEntity find = repository.findOne(clientData.getId());
        assertNotNull(find);
        assertEquals(find.getFirstName(), clientData.getFirstName());
        assertEquals(find.getLastName(), clientData.getLastName());
    }

    @Test
    public void findExistedClientById() {
        Client find = service.findOne(clientData.getId());
        assertNotNull(find);
        assertEquals(clientData.getFirstName(), find.getFirstName());
        assertEquals(clientData.getLastName(), find.getLastName());
    }

    /**
     * Test throw exception then item not exist.
     */
    @Test
    public void throwClientExceptionWhenFindNotExistedClientById() {
        final long notExistedId = 52345345L;
        thrown.expect(expNotFoundMatcher(notExistedId));

        service.findOne(notExistedId);
    }

    @Test
    public void canCreateNewClient() {
        Client clientNew = new Client();
        clientNew.setFirstName("Max");
        clientNew.setMiddleName("Mad");
        clientNew.setBirthday(new Date());

        clientNew = service.create(clientNew);
        assertNotNull(clientNew.getId());

        Client clientFromDb = ClientConverter.toModel(repository.findOne(clientNew.getId()));
        assertEquals(clientNew.getFirstName(), clientFromDb.getFirstName());
        assertEquals(clientNew.getMiddleName(), clientFromDb.getMiddleName());
    }

    /**
     * Test throw validation exception when try to create item with id.
     */
    @Test
    public void throwClientExceptionWhenCreateNewClientWithId() {
        thrown.expect(expValidationMatcher());

        Client clientNew = new Client();
        clientNew.setMiddleName("King");
        clientNew.setFirstName("Arthur");
        clientNew.setId(12L);

        service.create(clientNew);
    }

    @Test
    public void canUpdateExistedClient() {

        final String newMiddleName = "Daredevil";
        clientData.setMiddleName(newMiddleName);
        service.update(clientData.getId(), clientData);

        Client clientFromDb = ClientConverter.toModel(repository.findOne(clientData.getId()));
        assertEquals(newMiddleName, clientFromDb.getMiddleName());
    }

    /**
     * Test throw validation exception when try to update client without id.
     */
    @Test
    public void throwClientExceptionWhenUpdateClientWithoutId() {
        thrown.expect(expValidationMatcher());

        service.update(null, new Client("Mistery", "Person"));
    }

    /**
     * Test throw not found exception when try to update not existed client.
     */
    @Test
    public void throwClientExceptionWhenUpdateNotExistedClient() {
        final long notExistedId = 4234534543L;

        thrown.expect(expNotFoundMatcher(notExistedId));

        Client client = new Client("Jim", "Jarmusch");
        client.setId(notExistedId);

        service.update(notExistedId, client);
    }

    /**
     * Test throw not found exception when try to delete not existed client.
     */
    @Test
    public void throwClientExceptionWhenDeleteNotExistedClient() {
        final long notExistedId = 67567567L;

        thrown.expect(expNotFoundMatcher(notExistedId));

        service.delete(notExistedId);
    }

    @Test
    public void rollbackTransactionThenDeleteAccountsWithError() throws Exception {
        thrown.expect(ClientException.class);

        // given
        final long countClientsBefore = repository.count();
        BDDMockito.willThrow(new ClientException(MicroserviceException.ErrorTypes.bad_mks_request))
                .given(accountMksService)
                .deleteAccountsByClientId(Matchers.anyLong());

        // when
        try {
            service.delete(clientData.getId());
        } catch (Exception e) {
            // then
            final long countClientsAfter = repository.count();
            assertThat(countClientsAfter).isEqualTo(countClientsBefore);
            throw e;
        }

    }
    
    
}
