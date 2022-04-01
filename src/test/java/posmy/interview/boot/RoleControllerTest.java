package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class RoleControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    final String ENDPOINT = "/roles/";
    final String ROLE_DETAIL = ENDPOINT + 1;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    }

    @Test
    void viewAll() throws Exception {
        mvc.perform(get(ENDPOINT)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void viewById() throws Exception {
        mvc.perform(get(ROLE_DETAIL)).andExpect(status().is2xxSuccessful());
    }
}