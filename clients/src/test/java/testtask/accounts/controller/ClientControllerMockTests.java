
package testtask.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.DefaultResponseCreator.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import testtask.accounts.model.Client;
import testtask.accounts.service.ClientService;

/**
 *
 * @author Strannica
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientControllerMockTests {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController controller;

    private JacksonTester<Client> jacksonTester;

    @Before
    public void init() {
        // initialize jacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/client/46456435"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindById() throws Exception {

        // given
        final Client client = new Client();
        client.setFirstName("Stephen");
        client.setLastName("Hawking");
        client.setId(1L);
        BDDMockito.given(clientService.findOne(new Long(1))).willReturn(client);

        // when
        MockHttpServletResponse responce = mockMvc.perform(get("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responce.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(responce.getContentAsString()).isEqualTo(jacksonTester.write(client).getJson());

    }
}
