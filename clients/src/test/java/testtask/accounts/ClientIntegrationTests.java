package testtask.accounts;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import testtask.accounts.dao.ClientEntity;
import testtask.accounts.dao.ClientRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 26, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ClientsApplication.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class ClientIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ClientRepository repo;

    private ClientEntity entity;

    @Before
    public void init() {
        entity = new ClientEntity();
        entity.setFirstName("Arnold");
        entity.setLastName("Shvartsneger");
        entity.setMiddleName("123");
        entity = repo.save(entity);
    }

    
    @Test
    public void getClientFromRest() throws Exception {
    
        mockMvc.perform(get("/client/" + entity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", CoreMatchers.is(entity.getFirstName())));
    }

//    @Test
//    public void saveClientTest() {
//    
//    }
}
