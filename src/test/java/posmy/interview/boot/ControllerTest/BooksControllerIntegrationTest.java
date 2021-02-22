package posmy.interview.boot.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.controller.BooksController;
import posmy.interview.boot.model.Books;
import posmy.interview.boot.service.BooksService;

import org.mockito.Mockito;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(BooksController.class)
public class BooksControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BooksService booksService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenBooks_thenReturnJsonArray()
            throws Exception {

        Books book = new Books("Harry Porter", "YSK", "2010", "Available");

        List<Books> allBooks = Arrays.asList(book);

        given(booksService.getAllBooks()).willReturn(allBooks);

        mvc.perform(get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", CoreMatchers.is(book.getTitle())));
    }


}
