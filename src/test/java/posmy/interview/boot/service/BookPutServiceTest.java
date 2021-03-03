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
import posmy.interview.boot.error.BusinessException;
import posmy.interview.boot.error.NoSuchBookException;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.repos.BookRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookPutServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookPutService bookPutService;

    @Captor
    private final ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

    private Book existingBook;
    private BookPutRequest request;

    @BeforeEach
    void setup() {
        String id = "id001";
        existingBook = Book.builder()
                .id(id)
                .name("oldName")
                .desc("oldDesc")
                .imageUrl("oldImageUrl")
                .status(BookStatus.AVAILABLE)
                .lastUpdateDt(ZonedDateTime.now().minusDays(1))
                .build();
        request = BookPutRequest.builder()
                .id(id)
                .name("newName")
                .desc("newDesc")
                .imageUrl("newImageUrl")
                .status(BookStatus.BORROWED)
                .build();
    }

    @Test
    void givenIdExistThenPut() {
        when(bookRepository.findById(request.getId()))
                .thenReturn(Optional.of(existingBook));

        Book expectedBook = existingBook.toBuilder()
                .name(request.getName())
                .desc(request.getDesc())
                .imageUrl(request.getImageUrl())
                .status(request.getStatus())
                .build();

        bookPutService.execute(request);
        verify(bookRepository, times(1))
                .save(bookCaptor.capture());
        assertThat(bookCaptor.getValue())
                .usingRecursiveComparison().ignoringFields("lastUpdateDt")
                .isEqualTo(expectedBook);
    }

    @Test
    void givenIdNotExistThenThrowNoSuchBookException() {
        when(bookRepository.findById(request.getId()))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(NoSuchBookException.class,
                () -> bookPutService.execute(request));
        assertThat(exception.getErrorCode())
                .isEqualTo(10000);
        verify(bookRepository, times(0))
                .save(any());
    }
}