package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.model.request.BookDeleteRequest;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.service.BookAddService;
import posmy.interview.boot.service.BookDeleteService;
import posmy.interview.boot.service.BookPutService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LibrarianControllerTest {
    @Mock
    private BookAddService bookAddService;
    @Mock
    private BookPutService bookPutService;
    @Mock
    private BookDeleteService bookDeleteService;

    @InjectMocks
    private LibrarianController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void givenNameWhenBookAddThenSuccess() throws Exception {
        BookAddRequest request = BookAddRequest.builder()
                .name("book001")
                .desc("book 001 is...")
                .imageUrl("https://image")
                .build();

        when(bookAddService.execute(request))
                .thenReturn(new Book());

        mockMvc.perform(post("/v1/librarian/book")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").hasJsonPath())
                .andExpect(jsonPath("$.status").hasJsonPath())
                .andExpect(jsonPath("$.desc").hasJsonPath())
                .andExpect(jsonPath("$.imageUrl").hasJsonPath())
                .andExpect(jsonPath("$.lastUpdateDt").hasJsonPath());
        verify(bookAddService, times(1))
                .execute(request);
    }

    @Test
    void givenBlankNameWhenBookAddThenError() throws Exception {
        BookAddRequest request = BookAddRequest.builder()
                .name("")
                .desc("some desc")
                .imageUrl("http://image")
                .build();

        mockMvc.perform(post("/v1/librarian/book")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenModifiedBookWhenBookPutThenSuccess() throws Exception {
        BookPutRequest request = BookPutRequest.builder()
                .id("id001")
                .name("name")
                .desc("desc")
                .imageUrl("https://image")
                .status(BookStatus.AVAILABLE)
                .build();

        when(bookPutService.execute(request))
                .thenReturn(new Book());

        mockMvc.perform(put("/v1/librarian/book")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        verify(bookPutService, times(1))
                .execute(request);
    }

    @Test
    void givenBlankIdWhenBookPutThenError() throws Exception {
        BookPutRequest request = BookPutRequest.builder()
                .name("name")
                .desc("desc")
                .imageUrl("https://image")
                .status(BookStatus.AVAILABLE)
                .build();

        mockMvc.perform(put("/v1/librarian/book")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenBlankNameWhenBookPutThenError() throws Exception {
        BookPutRequest request = BookPutRequest.builder()
                .id("id001")
                .name(null)
                .desc("desc")
                .imageUrl("https://image")
                .status(BookStatus.BORROWED)
                .build();

        mockMvc.perform(put("/v1/librarian/book")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenBlankStatusWhenBookPutThenError() throws Exception {
        BookPutRequest request = BookPutRequest.builder()
                .id("id001")
                .name("name")
                .desc("desc")
                .imageUrl("https://image")
                .status(null)
                .build();

        mockMvc.perform(put("/v1/librarian/book")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenIdWhenBookDeleteThenSuccess() throws Exception {
        String deleteId = "id001";
        BookDeleteRequest request = BookDeleteRequest.builder()
                .id(deleteId)
                .build();

        mockMvc.perform(delete("/v1/librarian/book/" + deleteId)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(bookDeleteService, times(1))
                .execute(request);
    }

    @Test
    void givenBlankIdWhenBookDeleteThenError() throws Exception {
        String deleteId = "";
        BookDeleteRequest request = BookDeleteRequest.builder()
                .id(deleteId)
                .build();

        mockMvc.perform(delete("/v1/librarian/book/" + deleteId)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(bookDeleteService, times(0))
                .execute(any());
    }
}
