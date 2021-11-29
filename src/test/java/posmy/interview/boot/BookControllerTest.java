package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.entity.Book;

import java.io.File;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @Order(1)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to add new book")
    void addBookForLibrarian() throws Exception {
        Book user = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(post("/book/add").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to add existing book")
    void addExistingBookForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(post("/book/add").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to update existing book")
    void updateExistingBookForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/update").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to delete existing book")
    void deleteExistingBookForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/delete").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to update non existing book")
    void updateNewBookForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/update").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(6)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to delete non existing member")
    void deleteNewBookForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/delete").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(7)
    @WithMockUser(authorities = "{LIBRARIAN,MEMBER}")
    @DisplayName("Librarian/Member to view all books")
    void viewAllBook() throws Exception {
        mvc.perform(get("/book/view/all?page=0").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithMockUser(authorities = "{LIBRARIAN,MEMBER}")
    @DisplayName("Librarian/Member to view all book with wrong param")
    void viewAllBookWithWrongParam() throws Exception {
        mvc.perform(get("/book/view/all?page=-14").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(9)
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member to borrow unavailable book")
    void borrowUnavailableBookForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/borrow").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(10)
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member to borrow book without login")
    void borrowBookWithoutLoginForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/borrow").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(11)
    @WithMockUser(username = "test1", password = "12345", authorities = "MEMBER")
    @DisplayName("Member to borrow book")
    void borrowBookForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/borrow").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    @WithMockUser(username = "test1", password = "12345", authorities = "MEMBER")
    @DisplayName("Member to borrow book that someone else has borrowed")
    void borrowBookThatSomeoneHasBorrowedForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/borrow").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(13)
    @WithMockUser(username = "test2", password = "12345", authorities = "MEMBER")
    @DisplayName("Member to return book that someone else has borrowed")
    void returnBookThatSomeoneHasBorrowedForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/return").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(14)
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member to return book without login")
    void returnBookWithoutLoginForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/return").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(15)
    @WithMockUser(username = "test1", password = "12345", authorities = "MEMBER")
    @DisplayName("Member to return book")
    void returnBookForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/return").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(16)
    @WithMockUser(username = "test1", password = "12345", authorities = "MEMBER")
    @DisplayName("Member to return book that is still available")
    void returnBookThatIsNotBorrowedForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/return").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
