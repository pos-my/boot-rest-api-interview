package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.repos.BookRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookAddServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookAddService bookAddService;

    @Captor
    private final ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

    private BookAddRequest request;

    @BeforeEach
    void setup() {
        request = BookAddRequest.builder()
                .name("book name")
                .desc("book description")
                .imageUrl("https://image")
                .build();
    }

    @Test
    void givenNameThenSaveBook() {
        Book expectedBook = Book.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .desc(request.getDesc())
                .imageUrl(request.getImageUrl())
                .status(BookStatus.AVAILABLE)
                .lastUpdateDt(ZonedDateTime.now())
                .build();

        bookAddService.execute(request);

        verify(bookRepository, times(1))
                .save(bookCaptor.capture());
        assertThat(bookCaptor.getValue())
                .usingRecursiveComparison().ignoringFields("id", "lastUpdateDt")
                .isEqualTo(expectedBook);
    }
}