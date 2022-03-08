package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.model.Book;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void viewBooks() throws Exception {
        mvc.perform(get("/viewBooks"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addBooks() throws Exception {
        Book book1 = new Book();
        book1.setBookTitle("Harry Potter and The Sorcerer's Stone");
        book1.setBookStatus("AVAILABLE");
        Book book2 = new Book();
        book2.setBookTitle("Harry Potter and the Chamber of Secrets");
        book2.setBookStatus("AVAILABLE");
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        String bookListString = mapper.writeValueAsString(bookList);
        mvc.perform(post("/addBooks").contentType(MediaType.APPLICATION_JSON).content(bookListString))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateBooks() throws Exception {
        Book book3 = new Book();
        book3.setBookId(3);
        book3.setBookTitle("Harry Potter and the Prisoner of Azkaban");
        book3.setBookStatus("BORROWED");
        Book book4 = new Book();
        book4.setBookId(4);
        book4.setBookTitle("Harry Potter and the Goblet of Fire");
        book4.setBookStatus("BORROWED");
        List<Book> bookList = new ArrayList<>();
        bookList.add(book3);
        bookList.add(book4);

        String bookListString = mapper.writeValueAsString(bookList);
        mvc.perform(put("/updateBooks").contentType(MediaType.APPLICATION_JSON).content(bookListString))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeBook() throws Exception {
        String bookIdToBeRemoved = "4";
        mvc.perform(delete("/removeBook/" + bookIdToBeRemoved))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void borrowBook() throws Exception {
        String bookIdToBeBorrowed = "5";
        mvc.perform(put("/borrowBook/" + bookIdToBeBorrowed))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void returnBook() throws Exception {
        String bookIdToBeReturned = "6";
        mvc.perform(put("/returnBook/" + bookIdToBeReturned))
                .andExpect(status().is2xxSuccessful());
    }
}