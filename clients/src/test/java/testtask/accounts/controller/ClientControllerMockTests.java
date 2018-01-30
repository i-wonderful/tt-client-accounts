package testtask.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import testtask.accounts.model.Client;
import testtask.accounts.service.ClientService;
import testtask.accounts.exception.ApiErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.exception.SQLGrammarException;
import org.mockito.Matchers;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.client.ResourceAccessException;
import testtask.accounts.exception.ClientExceptionHandler;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.MicroserviceException;

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
    private JacksonTester<ApiErrorDto> testerErrorJson;

    @Before
    public void init() {
        // initialize jacksonTester
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ClientExceptionHandler()) // Init Exception Handler
                .build();

    }

    @Test
    public void shouldThrowJsonErrorWhenSearchingNotExistingClient() throws Exception {

        // given
        final long notExistedClientId = 435435446L;
        BDDMockito.given(clientService.findOne(notExistedClientId))
                .willThrow(new ClientException(notExistedClientId, MicroserviceException.ErrorTypes.not_found));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/client/" + notExistedClientId))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiErrorDto errorDto = testerErrorJson.parseObject(response.getContentAsString());
        assertThat(errorDto.getErrType()).isEqualTo(MicroserviceException.ErrorTypes.not_found.name());
        assertThat(errorDto.getMessage()).isEqualTo(ClientException.getStandartInfo(MicroserviceException.ErrorTypes.not_found, notExistedClientId));
    }

    @Test
    public void findExistedClientById() throws Exception {

        // given
        final Client client = new Client();
        client.setFirstName("Stephen");
        client.setLastName("Hawking");
        client.setId(1L);
        BDDMockito.given(clientService.findOne(1L)).willReturn(client);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(jacksonTester.write(client).getJson());

    }

    @Test
    public void canCreateNewClient() throws Exception {

        // given
        final Client client = new Client("Christopfer", "Nolan");
        BDDMockito.given(clientService.create(BDDMockito.any())).willAnswer((invocation) -> {
            client.setId(2L);
            return client;
        });

        // when
        MockHttpServletResponse responce = mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(client).getJson()))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Client savedClient = jacksonTester.parseObject(responce.getContentAsString());
        assertThat(savedClient.getId()).isEqualTo(2L);
        assertThat(savedClient.getFirstName()).isEqualTo(client.getFirstName());
        assertThat(savedClient.getLastName()).isEqualTo(client.getLastName());
    }

    @Test
    public void canUpdateClient() throws IOException, Exception {

        // given
        final long clientId = 3L;
        Client client = new Client("Tim", "Burton");
        BDDMockito.given(clientService.update(clientId, client)).willReturn(client);

        // when
        MockHttpServletResponse responce = mockMvc.perform(put("/client/" + clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(client).getJson()))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void canDeleteClient() throws Exception {

        // when
        MockHttpServletResponse responce = mockMvc.perform(delete("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getErrorWhenMksAccountsInNotAvailable() throws Exception {

        final long clientId = 1L;
        // given
        BDDMockito.given(clientService.findWithAccounts(clientId)).willThrow(new ResourceAccessException("Resource Exception"));
        
        // when
        MockHttpServletResponse responce = mockMvc.perform(get("/client/withAccounts/" + clientId))
                .andDo(print())
                .andReturn().getResponse();
        
        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        
    }
    
    @Test
    public void getInternalError() throws Exception{
    
        // given
        BDDMockito.given(clientService.findOne(Matchers.any())).willThrow(new InvalidDataAccessResourceUsageException("Database is not available"));
        
        // when  
        MockHttpServletResponse responce = mockMvc.perform(get("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(responce.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ApiErrorDto errorDto = testerErrorJson.parseObject(responce.getContentAsString());
        assertThat(errorDto.getErrType()).isEqualTo(MicroserviceException.ErrorTypes.db_error.toString());
    }
}
