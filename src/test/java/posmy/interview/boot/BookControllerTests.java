package posmy.interview.boot;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import posmy.interview.boot.controller.BookController;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.book.BookService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@WebMvcTest(BookController.class)
public class BookControllerTests extends SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(authorities={"MEMBER"})
    void test_get_book() throws Exception {
        final String EXPECTED_ISBN = "testisbn";
        Book bookStub = new Book();
        bookStub.setIsbn(EXPECTED_ISBN);
        when(bookService.getBook(any(Long.class))).thenReturn(bookStub);
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/book/{bookId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals("Response Status OK", HttpStatus.OK.value(), status);
        verify(bookService).getBook(any(Long.class));
        Book resultBook = new Gson().fromJson(result.getResponse().getContentAsString(), Book.class);
        assertThat(resultBook).isNotNull();
        assertThat(resultBook.getIsbn()).isEqualTo(EXPECTED_ISBN);
    }

    @Test
    void test_unauthorized_get_book() throws Exception {
        when(bookService.getBook(any(Long.class))).thenReturn(new Book());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/book/{bookId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals("Response Status Unauthorized", HttpStatus.UNAUTHORIZED.value(), status);
    }
}
