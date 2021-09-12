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
        testBook1.setTitle("The Lord of the Rings");
        testBook1.setAuthor("J. R. R. Tolkien");
        testBook1.setPublishedYear("1954");
        testBook1.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook1);

        testBook2 = new Book();
        testBook2.setTitle("Klara and the Sun");
        testBook2.setAuthor("Kazuo Ishiguro");
        testBook2.setPublishedYear("2021");
        testBook2.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook2);

        testBook3 = new Book();
        testBook3.setTitle("The Remains of the Day");
        testBook3.setAuthor("Kazuo Ishiguro");
        testBook3.setPublishedYear("1989");
        testBook3.setStatus(BookStatus.BORROWED);
        bookRepository.save(testBook3);
    }

    @Test
    @Order(1)
    void findBookById_BookExist_ReturnBook() {
        long id = 1;
        var result = bookRepository.findById(id);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(testBook1.getTitle());
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
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @Order(4)
    void addNewBook_Success() {
        Book testBook = new Book();
        testBook.setTitle("Sophie's World");
        testBook.setAuthor("Jostein Gaarder");
        testBook.setPublishedYear("1991");
        testBook.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(testBook);

        var result = bookRepository.findByTitle("Sophie's World");
        assertThat(result.get().getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    @Order(5)
    void findBookByTitle_ReturnBook() {
        var result = bookRepository.findByTitle("The Lord of the Rings");
        assertThat(result.get().getTitle()).isEqualTo(testBook1.getTitle());
    }

    @Test
    @Order(6)
    void findBookByAuthor_ReturnBook() {
        var result = bookRepository.findByAuthor("Kazuo Ishiguro");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Order(7)
    void findBookByPublishedYear_ReturnBook() {
        var result = bookRepository.findByPublishedYear("2021");
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook2.getTitle());
    }

    @Test
    @Order(8)
    void findBookByStatus_StatusBorrowed_ReturnBook() {
        var result = bookRepository.findByStatus(BookStatus.BORROWED);
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook3.getTitle());
    }

    @Test
    @Order(9)
    void findBookByTitleAndAuthor_ReturnBook() {
        var result = bookRepository.findByTitleAndAuthor("The Lord of the Rings", "J. R. R. Tolkien");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(testBook1.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(testBook1.getAuthor());
    }

}