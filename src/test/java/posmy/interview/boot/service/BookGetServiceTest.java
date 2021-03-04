package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.BookGetRequest;
import posmy.interview.boot.model.response.BookGetResponse;
import posmy.interview.boot.repos.BookRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookGetServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookGetService bookGetService;

    private BookGetRequest request;
    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 2);
        request = BookGetRequest.builder()
                .pageable(pageable)
                .build();
    }

    @Test
    void givenPageAndSizeWhenBookGetThenReturnPageOfBook() {
        when(bookRepository.findAll(pageable))
                .thenReturn(setupPageOfBook(3));

        BookGetResponse response = bookGetService.execute(request);
        verify(bookRepository, times(1))
                .findAll(pageable);
        assertThat(response.getPage().getContent().size())
                .isEqualTo(pageable.getPageSize());
    }

    @Test
    void givenPageExceedsTotalPagesWhenBookGetThenReturnEmptyPageWithTotalPageInfo() {
        pageable = PageRequest.of(3, 2);
        request.setPageable(pageable);
        when(bookRepository.findAll(pageable))
                .thenReturn(setupPageOfBook(3));

        BookGetResponse response = bookGetService.execute(request);
        verify(bookRepository, times(1))
                .findAll(pageable);
        assertThat(response.getPage().getContent().size())
                .isZero();
        assertThat(response.getPage().getTotalPages())
                .isLessThan(3);
    }

    private Page<Book> setupPageOfBook(int size) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Book book = Book.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Book " + i)
                    .desc("Book Desc " + i)
                    .imageUrl("http://image_book_" + i)
                    .status((i & 1) == 0 ? BookStatus.BORROWED : BookStatus.AVAILABLE)
                    .lastUpdateDt(ZonedDateTime.now().minusDays(i))
                    .build();
            books.add(book);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        if(start > books.size())
            return new PageImpl<>(new ArrayList<>(), pageable, books.size());
        return new PageImpl<>(books.subList(start, end), pageable, books.size());
    }
}