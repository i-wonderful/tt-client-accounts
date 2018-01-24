/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessttask.accounts.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import testtask.accounts.rest.ClientController;

/**
 *
 * @author Strannica
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ClientController.class})
//@AutoConfigureMockMvc
@RestClientTest(ClientController.class)
public class ClientControllerRestTest {
    
//    @Autowired
//    MockRestServiceServer server;
//    @Autowired
//    MockMvc mockMvc;
    
    @Test
    public void testGo(){
       
//        server.expect(MockRestRequestMatchers.requestTo("/wer"))
//                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
        Assert.assertTrue(true);
    }
}
