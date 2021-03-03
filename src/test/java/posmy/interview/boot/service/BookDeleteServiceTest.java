package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import posmy.interview.boot.model.request.BookDeleteRequest;
import posmy.interview.boot.repos.BookRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookDeleteServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookDeleteService bookDeleteService;

    private BookDeleteRequest request;

    @BeforeEach
    void setup() {
        request = BookDeleteRequest.builder()
                .id("id001")
                .build();
    }

    @Test
    void givenIdThenBookDelete() {
        bookDeleteService.execute(request);
        verify(bookRepository, times(1))
                .deleteById(request.getId());
    }

    @Test
    void givenNonExistingIdThenDoNothing() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(bookRepository).deleteById(request.getId());

        bookDeleteService.execute(request);
        verify(bookRepository, times(1))
                .deleteById(request.getId());
    }

    @Test
    void givenIdWhenDataResourceExceptionThenThrow() {
        doThrow(new DataAccessResourceFailureException("Fail to access datasource"))
                .when(bookRepository).deleteById(request.getId());

        assertThrows(DataAccessResourceFailureException.class,
                () -> bookDeleteService.execute(request));
        verify(bookRepository, times(1))
                .deleteById(request.getId());
    }
}