package posmy.interview.boot.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import posmy.interview.boot.domain.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.repo.BookRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {
    Logger logger = LoggerFactory.getLogger(BookControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Add Book with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testAddBook() throws Exception {
        String isbn = "12345";

        Book book = Book.builder().isbn(isbn).bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        mockMvc.perform(post("/librarian/book")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        Optional<Book> savedBook = bookRepository.findBookByIsbn(isbn);
        assertThat(savedBook.isPresent()).isEqualTo(true);
        if (savedBook.isPresent()) {
            Book currBook = savedBook.get();
            assertThat(book.equals(currBook)).isTrue();
        }
    }

    @Test
    @DisplayName("Update Book with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testUpdateBook() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Book book = Book.builder().isbn("999999").bookStatus(BookStatus.AVAILABLE).title("Default Book")
                .author("For Update").createdDate(date).id(uuid).build();
        bookRepository.save(book);

        String changedData = "Updated";

        book.setBookStatus(BookStatus.BORROWED);
        book.setAuthor(changedData);
        book.setTitle(changedData);

        mockMvc.perform(put("/librarian/book")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());

        Optional<Book> savedBook = bookRepository.findById(uuid);
        assertThat(savedBook.isPresent()).isEqualTo(true);
        if (savedBook.isPresent()) {
            Book currBook = savedBook.get();
            assertThat(currBook.getBookStatus().equals(BookStatus.BORROWED)).isTrue();
            assertThat(currBook.getAuthor().equals(changedData)).isTrue();
            assertThat(currBook.getTitle().equals(changedData)).isTrue();
        }
    }

    @Test
    @DisplayName("Delete Book with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testDeleteBook() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Book book = Book.builder().isbn("999998").bookStatus(BookStatus.AVAILABLE).title("Default Book")
                .author("For Delete").createdDate(date).id(uuid).build();
        Book savedBook = bookRepository.save(book);

        mockMvc.perform(delete("/librarian/book")
                .contentType("application/json")
                .param("id", savedBook.getId().toString()))
                .andExpect(status().isOk());

        Optional<Book> deletedBook = bookRepository.findById(savedBook.getId());
        assertThat(deletedBook.isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("Get available book with Member role")
    @WithMockUser(roles = "MEMBER")
    void testGetAvailableBooksList() throws Exception {
        MvcResult result = mockMvc.perform(get("/member/book")
                .contentType("application/json")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn();
        logger.info("Result: {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Borrow book with Member role")
    @WithMockUser(username = "member", roles = "MEMBER")
    void testBorrowBook() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Book book = Book.builder().isbn("999997").bookStatus(BookStatus.AVAILABLE).title("Default Book")
                .author("For Borrow").createdDate(date).id(uuid).build();
        bookRepository.save(book);

        mockMvc.perform(put("/member/book/borrow")
                .contentType("application/json")
                .param("bookId", uuid.toString()))
                .andExpect(status().isOk());

        Optional<Book> updatedBook = bookRepository.findById(uuid);
        assertThat(updatedBook.isPresent()).isEqualTo(true);
        if (updatedBook.isPresent()) {
            Book currBook = updatedBook.get();
            assertThat(currBook.getBookStatus()).isEqualTo(BookStatus.BORROWED);
        }
    }

    @Test
    @DisplayName("Return book with Member role")
    @WithMockUser(roles = "MEMBER")
    void testReturnBook() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Book book = Book.builder().isbn("999996").bookStatus(BookStatus.BORROWED).title("Default Book")
                .author("For Return").createdDate(date).id(uuid).build();
        bookRepository.save(book);

        mockMvc.perform(put("/member/book/return")
                .contentType("application/json")
                .param("id", uuid.toString()))
                .andExpect(status().isOk());

        Optional<Book> updatedBook = bookRepository.findById(uuid);
        assertThat(updatedBook.isPresent()).isEqualTo(true);
        if (updatedBook.isPresent()) {
            Book currBook = updatedBook.get();
            assertThat(currBook.getBookStatus()).isEqualTo(BookStatus.AVAILABLE);
        }
    }
}
