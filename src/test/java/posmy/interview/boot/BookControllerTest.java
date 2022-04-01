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

import posmy.interview.boot.book.requests.NewBookRequest;
import posmy.interview.boot.book.requests.UpdateBookRequest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    final String ENDPOINT = "/book/";
    final String UPDATE_DETAIL_BOOK = ENDPOINT + 1;
    final String DELETE_DETAIL_BOOK = ENDPOINT + 2;

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
        mvc.perform(get(UPDATE_DETAIL_BOOK)).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void add() throws Exception {
        NewBookRequest newBookRequest = new NewBookRequest();

        newBookRequest.setAuthor("AuthorDummy");
        newBookRequest.setIsnb("1234-5678-9101");
        newBookRequest.setTitle("Atlas 101");
        mvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBookRequest))).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void update() throws Exception {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setAuthor("AuthorName");
        updateBookRequest.setTitle("Atlas 202");
        updateBookRequest.setStatus("BORROWED");
        updateBookRequest.setUser("UserMember");
        mvc.perform(put(UPDATE_DETAIL_BOOK).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookRequest))).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "UserLibrarian")
    void remove() throws Exception {
        mvc.perform(delete(DELETE_DETAIL_BOOK)).andExpect(status().is2xxSuccessful());
    }
}