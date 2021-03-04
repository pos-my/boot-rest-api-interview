package posmy.interview.boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.*;
import posmy.interview.boot.model.response.BookGetResponse;
import posmy.interview.boot.service.BookBorrowService;
import posmy.interview.boot.service.BookGetService;
import posmy.interview.boot.service.BookReturnService;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {
    @Mock
    private BookGetService bookGetService;
    @Mock
    private BookBorrowService bookBorrowService;
    @Mock
    private BookReturnService bookReturnService;

    @InjectMocks
    private MemberController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenBookGetThenResponseWithPageOfBook() throws Exception {
        int page = 0;
        int size = 2;
        BookGetRequest request = BookGetRequest.builder()
                .pageable(PageRequest.of(page, size))
                .build();
        BookGetResponse response = setupBookGetResponse();
        when(bookGetService.execute(request))
                .thenReturn(response);

        mockMvc.perform(get("/v1/member/book")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.page.content").isArray())
                .andExpect(jsonPath("$.page.content[0].id").hasJsonPath())
                .andExpect(jsonPath("$.page.content[0].name").hasJsonPath())
                .andExpect(jsonPath("$.page.content[0].desc").hasJsonPath())
                .andExpect(jsonPath("$.page.content[0].status").hasJsonPath());
    }

    @Test
    void whenBookBorrowThenSuccess() throws Exception {
        Principal principal = mock(Principal.class);
        String bookId = "book001-1";
        String username = "user001";
        when(principal.getName())
                .thenReturn(username);
        BookBorrowRequest expectedRequest = BookBorrowRequest.builder()
                .bookId(bookId)
                .username(username)
                .build();

        mockMvc.perform(patch("/v1/member/book/borrow/" + bookId)
                .principal(principal)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(bookBorrowService, times(1))
                .execute(expectedRequest);
    }

    @Test
    void whenBookReturnThenSuccess() throws Exception {
        Principal principal = mock(Principal.class);
        String bookId = "book001-1";
        String username = "user001";
        when(principal.getName())
                .thenReturn(username);
        BookReturnRequest expectedRequest = BookReturnRequest.builder()
                .bookId(bookId)
                .username(username)
                .build();

        mockMvc.perform(patch("/v1/member/book/return/" + bookId)
                .principal(principal)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(bookReturnService, times(1))
                .execute(expectedRequest);
    }

    private BookGetResponse setupBookGetResponse() {
        Book book1 = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("Book 1")
                .desc("Book 1 Desc")
                .imageUrl("http://image_book_1")
                .status(BookStatus.AVAILABLE)
                .lastUpdateDt(ZonedDateTime.now())
                .build();
        Book book2 = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("Book 2")
                .desc("Book 2 Desc")
                .imageUrl("http://image_book_2")
                .status(BookStatus.BORROWED)
                .lastUpdateDt(ZonedDateTime.now().minusDays(1))
                .build();
        return BookGetResponse.builder()
                .page(new PageImpl<>(List.of(book1, book2)))
                .build();
    }
}
