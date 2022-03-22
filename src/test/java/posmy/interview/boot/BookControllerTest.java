package posmy.interview.boot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.model.request.BookRequest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BookControllerTest {

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
        mvc.perform(get("/book/viewAll")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void findById() throws Exception {
        mvc.perform(get("/book/view").param("id", "9443f8032a43444fa2f2d21f08d7b1df"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void remove() throws Exception {
        mvc.perform(delete("/book/remove").param("id", "9443f8032a43444fa2f2d21f08d7b1df"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void add() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("Test Book 1");
        bookRequest.setGenre("Test 1");

        mvc.perform(
                post("/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void update() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setId("9443f8032a43444fa2f2d21f08d7b1df");
        bookRequest.setTitle("Update Test");
        bookRequest.setGenre("Updated");
        bookRequest.setStatus("BORROWED");

        mvc.perform(
                        put("/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void borrow() throws Exception {
        mvc.perform(
                        put("/book/borrow")
                                .param("id", "9443f8032a43444fa2f2d21f08d7b1df"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void returnBook() throws Exception {
        mvc.perform(
                        put("/book/return")
                                .param("id", "2b4329cbf8534e349f625ff2574dd4f3"))
                .andExpect(status().is2xxSuccessful());
    }
}
