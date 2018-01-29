package testtask.accounts.service;

import java.util.Date;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import testtask.accounts.ClientsApplication;
import testtask.accounts.dao.ClientConverter;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;
import testtask.accounts.model.Client;
import static testtask.accounts.TestHelper.*;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 26, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class ClientServiceIntegrationTests {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    /* Test Data */
    private ClientEntity entity;
    private Client entityModel;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        entity = new ClientEntity();
        entity.setFirstName("Matthew");
        entity.setLastName("Murdock");
        entity.setMiddleName("Michael");
        entity = repository.save(entity);
        entityModel = ClientConverter.entityToModel(entity);
    }

    @After
    public void clear() {
        if (repository.exists(entity.getId())) {
            repository.delete(entity.getId());
        }
    }

    @Test
    public void testFindFromDb() {

        ClientEntity find = repository.findOne(entity.getId());
        assertNotNull(find);
        assertEquals(find.getFirstName(), entity.getFirstName());
        assertEquals(find.getLastName(), entity.getLastName());
    }

    @Test
    public void findExistedClientById() {
        Client find = service.findOne(entityModel.getId());
        assertNotNull(find);
        assertEquals(entityModel.getFirstName(), find.getFirstName());
        assertEquals(entityModel.getLastName(), find.getLastName());
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

        Client clientFromDb = ClientConverter.entityToModel(repository.findOne(clientNew.getId()));
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
        entityModel.setMiddleName(newMiddleName);
        service.update(entityModel.getId(), entityModel);

        Client clientFromDb = ClientConverter.entityToModel(repository.findOne(entityModel.getId()));
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

    @Test
    public void testDelete() {
        service.delete(entityModel.getId());

        assertFalse(repository.exists(entityModel.getId()));

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
    public void throwResourceExpWhenTryToUseExistedMicroservice(){
        thrown.expect(ResourceAccessException.class);
        
        service.findWithAccounts(entityModel.getId());
    }
}
