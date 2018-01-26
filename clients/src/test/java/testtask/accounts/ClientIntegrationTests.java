package testtask.accounts;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import org.apache.catalina.connector.OutputBuffer;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
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
import org.junit.Ignore;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import testtask.accounts.model.Client;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 26, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ClientsApplication.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Deprecated
public class ClientIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    
    // Mock Service? 
    
    
    
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
    @Ignore
    public void getClientFromRest() throws Exception {
    
        mockMvc.perform(get("/client/" + entity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", CoreMatchers.is(entity.getFirstName())))
                .andExpect(jsonPath("lastName", CoreMatchers.is(entity.getLastName())));
    }

    @Test
    public void saveClientTest() throws Exception {
    
        Client client = new Client();
        client.setFirstName("");
      
        mockMvc.perform(post("/client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Arthur\"}"))
                .andExpect(status().isOk());
    }
}
