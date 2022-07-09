package posmy.interview.boot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import posmy.interview.boot.constant.BookStatus;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.respository.BookRepository;

@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	private BookRepository underTest;
	
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	@Test
	void findIfBookExistByUsingBookId() {
		//give
		long id = Integer.toUnsignedLong(1);
		Book book = new Book();
		book.setBookId(id);
		book.setBookName("Wonderland");
		book.setStatus(BookStatus.AVAILABLE.name());
		
		underTest.save(book);
		
		//when
		Book expected = underTest.findByBookId(id);
		
		//then
		assertThat(expected).isNotNull();
	}
	
	@Test
	void findIfBookDoestNotExistByUsingBookId() {
		//give
		long id = Integer.toUnsignedLong(3);
		
		//when
		Book expected = underTest.findByBookId(id);
		
		//then
		assertThat(expected).isNull();
		
	}
}
