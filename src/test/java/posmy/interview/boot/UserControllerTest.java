
package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import posmy.interview.boot.user.request.NewUserRequest;
import posmy.interview.boot.user.request.UpdateUserRequest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    final String ENDPOINT = "/user/";
    final String UPDATE_DETAIL_USER = ENDPOINT + 1;
    final String DELETE_DETAIL_USER = ENDPOINT + 3;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void viewAll() throws Exception {
        mvc.perform(get(ENDPOINT)).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void viewById() throws Exception {
        mvc.perform(get(UPDATE_DETAIL_USER)).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void remove() throws Exception {
        mvc.perform(delete(DELETE_DETAIL_USER)).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void add() throws Exception {
        NewUserRequest userRequest = new NewUserRequest();
        userRequest.setUsername("NewUser");
        userRequest.setPassword("password");
        userRequest.setRole("MEMBER");
        mvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void update() throws Exception {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setPassword("passwordNew");
        userRequest.setRole("MEMBER");
        mvc.perform(put(UPDATE_DETAIL_USER).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))).andExpect(status().is2xxSuccessful());
    }
}