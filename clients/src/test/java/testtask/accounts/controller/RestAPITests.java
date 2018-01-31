package testtask.accounts.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Ignore;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

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


//    @Before
//    public void setUp() throws Exception {
//    }

    @Test
    @Ignore
    public void testGo() throws Exception {
        mockMvc.perform(get("/client/go"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("I'm client controller!")));
    }

//    @Test
//    public void testAccountsAll() throws Exception {
//        mockMvc.perform(get("/accounts/all"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @Ignore
    public void testClientGetById() throws Exception {
        mockMvc.perform(get("/client/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

//	@Test
//	public void apiControllerShouldReturnVisits() throws Exception {
//		mockMvc.perform(get("/"));
//
//		mockMvc.perform(get("/api/visits"))
//				.andExpect(jsonPath("$.*.description", iterableWithSize(1)));
//	}
}
