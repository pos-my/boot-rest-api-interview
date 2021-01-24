package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import posmy.interview.boot.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    private final Book book = new Book("Meaning of life", Status.AVAILABLE);
    private final Book book2 = new Book("Harry Potter", Status.BORROWED);

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        bookRepository.saveAll(Arrays.asList(book, book2));
        bookRepository.flush();
    }

    @Test
    void whenFindAll_thenReturnAllMembers() {
        final List<Book> books = bookRepository.findAll();
        assertThat(books)
                .hasSize(2)
                .containsExactly(book, book2);
    }

    @Test
    void whenFindByName_thenReturnMember() {
        final Optional<Book> actualMember = bookRepository.findById(book.getId());
        final Optional<Book> actualMember2 = bookRepository.findById(book2.getId());

        assertThat(actualMember)
                .isPresent()
                .isEqualTo(Optional.of(book));

        assertThat(actualMember2)
                .isPresent()
                .isEqualTo(Optional.of(book2));
    }

    @Test
    void whenUpdate_thenReturnUpdatedMember() {
        book2.setStatus(Status.AVAILABLE);
        bookRepository.saveAndFlush(book2);

        final Optional<Book> actualMember2 = bookRepository.findById(book2.getId());

        assertThat(actualMember2)
                .isPresent()
                .isEqualTo(Optional.of(book2));
    }

    @Test
    void whenDeleteById_thenReturnNull() {
        bookRepository.deleteById(book2.getId());

        final Optional<Book> actualMember2 = bookRepository.findById(book2.getId());
        assertThat(actualMember2).isNotPresent();
    }
}
