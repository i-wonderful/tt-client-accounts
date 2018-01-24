
package testtask.accounts.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import testtask.accounts.rest.ClientController;

/**
 *
 * @author Strannica
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestAPITests {

    @Autowired
    private MockMvc mockMvc;

//	@Autowired
//	private VisitsRepository visitsRepository;
    @Before
    public void setUp() throws Exception {
//		visitsRepository.deleteAll();
    }

    @Test
    public void testGo() throws Exception {
        mockMvc.perform(get("/client/go"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("I'm client controller!")));
    }

//	@Test
//	public void apiControllerShouldReturnVisits() throws Exception {
//		mockMvc.perform(get("/"));
//
//		mockMvc.perform(get("/api/visits"))
//				.andExpect(jsonPath("$.*.description", iterableWithSize(1)));
//	}
}
