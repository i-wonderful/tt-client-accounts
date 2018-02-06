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
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResourceAccessException;
import testtask.accounts.exception.ApiErrorDto;
import testtask.accounts.exception.ClientException;
import testtask.accounts.exception.ClientExceptionHandler;
import testtask.accounts.model.Client;
import testtask.accounts.service.ClientService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static testtask.accounts.exception.MicroserviceException.ErrorTypes;

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
    private JacksonTester<ApiErrorDto> apiErrorJacksonTester;

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
                .willThrow(new ClientException(notExistedClientId, ErrorTypes.not_found));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/client/" + notExistedClientId))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiErrorDto errorDto = apiErrorJacksonTester.parseObject(response.getContentAsString());
        assertThat(errorDto.getErrType()).isEqualTo(ErrorTypes.not_found.name());
        assertThat(errorDto.getMessage()).isEqualTo(ClientException.getStandartInfo(ErrorTypes.not_found, notExistedClientId));
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
        final long newClientId = 3L;
        final Client client = new Client("Christopfer", "Nolan");
        BDDMockito.given(clientService.create(BDDMockito.any())).willAnswer((invocation) -> {
            client.setId(newClientId);
            return client;
        });

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(client).getJson()))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Client savedClient = jacksonTester.parseObject(response.getContentAsString());
        assertThat(savedClient.getId()).isEqualTo(newClientId);
        assertThat(savedClient).isEqualToIgnoringGivenFields(client, "id");
    }

    @Test
    public void canUpdateClient() throws IOException, Exception {

        // given
        final long clientId = 3L;
        Client client = new Client("Tim", "Burton");
        BDDMockito.given(clientService.update(clientId, client)).willReturn(client);

        // when
        MockHttpServletResponse response = mockMvc.perform(put("/client/" + clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(client).getJson()))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void canDeleteClient() throws Exception {

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getErrorWhenDeleteClientAndExternalMksReturnError() throws Exception {
        // given
        final ClientException exp = new ClientException(ErrorTypes.bad_mks_request, "This is very sad");
        BDDMockito.willThrow(exp).given(clientService).delete(anyLong());

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ApiErrorDto errorDto = apiErrorJacksonTester.parseObject(response.getContentAsString());
        assertThat(errorDto.getMessage()).isEqualTo(exp.getMessage());
        assertThat(errorDto.getErrType()).isEqualTo(exp.getType().toString());
    }

    @Test
    public void getErrorWhenMksAccountsInNotAvailable() throws Exception {

        final long clientId = 1L;
        // given
        BDDMockito.given(clientService.findWithAccounts(clientId)).willThrow(new ResourceAccessException("Resource Exception"));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/client/withAccounts/" + clientId))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void getErrorThenInvalidDataAccess() throws Exception {

        // given
        BDDMockito.given(clientService.findOne(anyLong())).willThrow(new InvalidDataAccessResourceUsageException("Database is not available"));

        // when  
        MockHttpServletResponse response = mockMvc.perform(get("/client/1"))
                .andDo(print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ApiErrorDto errorDto = apiErrorJacksonTester.parseObject(response.getContentAsString());
        assertThat(errorDto.getErrType()).isEqualTo(ErrorTypes.db_error.toString());
    }
}
