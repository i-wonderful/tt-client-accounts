package tessttask.accounts.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.spriCngframework.test.context.junit4.SpringRunner;
//import org.spCringframework.test.web.client.MockRestServiceServer;
import testtask.accounts.rest.ClientController;

/**
 * Test For RST Controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ClientController.class})
//@RestClientTest(value = ClientController.class)
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

//    @Autowired
//    private MockRestServiceServer server;

    @Test
    public void testGo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/client/go"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
