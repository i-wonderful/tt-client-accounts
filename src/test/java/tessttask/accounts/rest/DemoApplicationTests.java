/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessttask.accounts.rest;

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

/**
 *
 * @author Strannica
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

//	@Autowired
//	private VisitsRepository visitsRepository;

	@Before
	public void setUp() throws Exception {
//		visitsRepository.deleteAll();
	}

	@Test
	public void indexControllerShouldReturnHtmlPage() throws Exception {
		mockMvc.perform( get("/client/go"))
				.andExpect(status().isOk());
//				.andExpect(content().string(containsString("Welcome to Spring")));
	}

//	@Test
//	public void apiControllerShouldReturnVisits() throws Exception {
//		mockMvc.perform(get("/"));
//
//		mockMvc.perform(get("/api/visits"))
//				.andExpect(jsonPath("$.*.description", iterableWithSize(1)));
//	}
}
