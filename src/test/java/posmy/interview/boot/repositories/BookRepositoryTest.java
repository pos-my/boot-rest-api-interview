package posmy.interview.boot.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookStatus;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;
    private Book testBook3;

    @BeforeAll
    void setUp() {
        testBook1 = new Book();
        testBook1.setTitle("The Grass is Always Greener");
        testBook1.setAuthor("Jeffrey Archer");
        testBook1.setPublishedYear("2011");
        testBook1.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook1);

        testBook2 = new Book();
        testBook2.setTitle("A Boy at Seven");
        testBook2.setAuthor("John Bidwell");
        testBook2.setPublishedYear("1960");
        testBook2.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook2);

        testBook3 = new Book();
        testBook3.setTitle("Bliss Feuille d'Album");
        testBook3.setAuthor("Katherine Mansfield");
        testBook3.setPublishedYear("1988");
        testBook3.setStatus(BookStatus.BORROWED);
        bookRepository.save(testBook3);
    }

    @Test
    @Order(1)
    void findBookById_BookExist_ReturnBook() {
        long id = 2;
        var result = bookRepository.findById(id);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(testBook2.getTitle());
    }

    @Test
    @Order(2)
    void findBookById_BookDoesNotExist_ReturnEmpty() {
        long id = 999;
        var result = bookRepository.findById(id);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @Order(3)
    void findAllBooks_ReturnBooks() {
        var result = bookRepository.findAll();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @Order(4)
    void addNewBook_Success() {
        Book testBook = new Book();
        testBook.setTitle("The Traveller's Story of a Terribly Strange Bed");
        testBook.setAuthor("Wilkie Collins");
        testBook.setPublishedYear("1889");
        testBook.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook);

        var result = bookRepository.findByTitle("The Traveller's Story of a Terribly Strange Bed");
        assertThat(result.get().getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    @Order(5)
    void findBookByAuthor_ReturnBook() {
        var result = bookRepository.findByAuthor("John Bidwell");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Order(6)
    void findBookByPublishedYear_ReturnBook() {
        var result = bookRepository.findByPublishedYear("2011");
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook1.getTitle());
    }

    @Test
    @Order(7)
    void findBookByStatus_StatusBorrowed_ReturnBook() {
        var result = bookRepository.findByStatus(BookStatus.BORROWED);
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook3.getTitle());
    }

}