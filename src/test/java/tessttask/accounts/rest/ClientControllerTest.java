package tessttask.accounts.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.spriCngframework.test.context.junit4.SpringRunner;
//import org.spCringframework.test.web.client.MockRestServiceServer;
import testtask.accounts.rest.ClientController;

/**
 *
 */
//@RunWith(SpringRunner.class)
@RestClientTest(value = ClientController.class)

public class ClientControllerTest {

//    @Autowired
//    MockRestServiceServer server;
    
//    @Test
    public void testGo() {

        Assert.assertTrue(true);
//        server.expect(requestT)
    }

}
