package testtask.accounts.service;

import java.util.Date;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 26, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application.properties")//-integrationtest
public class ClientServiceIntegrationTests {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    private ClientEntity entity;
    private Client entityModel;

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
        if(repository.exists(entity.getId()))
        repository.delete(entity.getId());
    }

    @Test
    public void testFindFromDb() {

        ClientEntity find = repository.findOne(entity.getId());
        assertNotNull(find);
        assertEquals(find.getFirstName(), entity.getFirstName());
        assertEquals(find.getLastName(), entity.getLastName());
    }

    @Test
    public void testFind() {
        Client find = service.findOne(entity.getId());
        assertNotNull(find);
        assertEquals(entityModel.getFirstName(), find.getFirstName());
        assertEquals(entityModel.getLastName(), find.getLastName());
    }

    @Test
    public void testSave() {
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
    
    @Test
    public void testDelete(){
        service.delete(entity.getId());
        
        assertFalse(repository.exists(entity.getId()));
    }
}
