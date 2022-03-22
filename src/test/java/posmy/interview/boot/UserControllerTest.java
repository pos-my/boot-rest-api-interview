package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.request.UserRequest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void viewAll() throws Exception {
        mvc.perform(get("/user/viewAll")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void findById() throws Exception {
        mvc.perform(get("/user/view").param("id", "10ea06009d45423593065b72e0b52e73"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void remove() throws Exception {
        mvc.perform(delete("/user/remove").param("id", "10ea06009d45423593065b72e0b52e73"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void add() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("Test1");
        userRequest.setEmail("test1@test.com");
        userRequest.setPassword("password");
        userRequest.setRole("MEMBER");

        mvc.perform(
                        post("/user/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void update() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setId("10ea06009d45423593065b72e0b52e73");
        userRequest.setUserName("SherlockHolmesUpdated");
        userRequest.setEmail("test1@test.com");
        userRequest.setPassword("password");
        userRequest.setRole("MEMBER");
        userRequest.setStatus("ACTIVE");

        mvc.perform(
                        put("/user/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().is2xxSuccessful());
    }
}
