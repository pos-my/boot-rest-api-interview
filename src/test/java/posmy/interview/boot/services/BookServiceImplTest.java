package posmy.interview.boot.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookStatus;
import posmy.interview.boot.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    private BookServiceImpl bookService;
    private BookRepository mockBookRepository;
    private Book mockBook1;
    private Book mockBook2;
    private Book mockBook4;
    private Optional<Book> mockBookObject1;
    private Optional<Book> mockBookObject2;
    private Optional<Book> mockBookObject3;
    private Optional<Book> mockBookObject4;
    private List<Book> mockBookList;

    @BeforeEach
    void setUp() {
        mockBookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(mockBookRepository);
        mockBook1 = new Book();
        mockBook1.setBookId(1);
        mockBook1.setTitle("The Grass is Always Greener");
        mockBook1.setAuthor("Jeffrey Archer");
        mockBook1.setPublishedYear("2011");
        mockBook1.setStatus(BookStatus.AVAILABLE);
        mockBookObject1 = Optional.of(mockBook1);
        mockBook2 = new Book();
        mockBook2.setBookId(2);
        mockBook2.setTitle("A Boy at Seven");
        mockBook2.setAuthor("Nicholas Sparks");
        mockBook2.setPublishedYear("1960");
        mockBook2.setStatus(BookStatus.AVAILABLE);
        mockBookObject2 = Optional.of(mockBook2);
        mockBookObject3 = Optional.empty();
        mockBookList = new ArrayList<>();
        mockBookList.add(mockBook1);
        mockBookList.add(mockBook2);
        mockBook4 = new Book();
        mockBook4.setBookId(4);
        mockBook4.setTitle("Bliss Feuille d'Album");
        mockBook4.setAuthor("Katherine Mansfield");
        mockBook4.setPublishedYear("1988");
        mockBook4.setStatus(BookStatus.BORROWED);
        mockBookObject4 = Optional.of(mockBook4);
    }

    @Test
    void getBookById_exists_returnBook() throws Exception {
        long id = 1;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject1);
        var result = bookService.getBookById(id);
        assertThat(result).isEqualTo(mockBook1);
    }

    @Test
    void getBookById_doesNotExist_throwsException() {
        long id = 0;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.getBookById(id);
        });
    }

    @Test
    void getAllBooks_booksExist_returnBooks() throws Exception {
        when(mockBookRepository.findAll()).thenReturn(mockBookList);
        var result = bookService.getAllBooks();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getAllBooks_booksDoNotExist_throwsException() {
        when(mockBookRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.getAllBooks();
        });
    }

    @Test
    void addBooks_success() throws Exception {
        Book bookRequest = new Book();
        bookRequest.setTitle("ABC");
        bookRequest.setAuthor("Mr.X");
        bookRequest.setPublishedYear("2000");
        bookRequest.setStatus(BookStatus.AVAILABLE);
        List<Book> bookListRequest = new ArrayList<>();
        bookListRequest.add(bookRequest);
        when(mockBookRepository.findByTitleAndAuthor(anyString(), anyString())).thenReturn(mockBookObject3);
        var result = bookService.addBooks(bookListRequest);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void addBooks_failure_throwsException() {
        List<Book> bookListRequest = new ArrayList<>();
        bookListRequest.add(mockBook1);
        when(mockBookRepository.findByTitleAndAuthor(anyString(), anyString())).thenReturn(mockBookObject1);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.addBooks(mockBookList);
        });
    }

    @Test
    void updateBookById_success() throws Exception {
        long id = 1;
        Book bookRequest = mockBook2;
        bookRequest.setPublishedYear("2020");
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject1);
        var result = bookService.updateBookById(id, bookRequest);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateBookById_failure_throwsException() {
        long id = 0;
        Book bookRequest = mockBook2;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.updateBookById(id, bookRequest);
        });
    }

    @Test
    void removeBookById_success() throws Exception {
        long id = 1;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject1);
        var result = bookService.removeBookById(id);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void removeBookById_failure_throwsException() {
        long id = 0;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.removeBookById(id);
        });
    }

    @Test
    void borrowBookById_success() throws Exception {
        long id = 1;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject1);
        var result = bookService.borrowBookById(id);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void borrowBookById_doesNotExist_throwsException() {
        long id = 0;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject3);
        var result = Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.borrowBookById(id);
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void borrowBookById_hasBeenBorrowed_throwsException() {
        long id = 4;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject4);
        var result = Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.borrowBookById(id);
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void returnBookById_success() throws Exception {
        long id = 4;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject4);
        var result = bookService.returnBookById(id);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void returnBookById_doesNotExist_throwsException() {
        long id = 0;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject3);
        var result = Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.returnBookById(id);
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void returnBookById_hasBeenReturned_throwsException() {
        long id = 2;
        when(mockBookRepository.findById(id)).thenReturn(mockBookObject2);
        var result = Assertions.assertThrows(CustomRestApiException.class, () -> {
            bookService.returnBookById(id);
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}