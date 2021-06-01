package posmy.interview.boot.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    
	List<Book> findAll();
	
	Optional<Book> findById( long id );
	
	boolean existsById( long id );
	
	Book findByTitle( String title );
	
	@Transactional
	@Modifying
	@Query(value="delete from books where id = :id", nativeQuery = true)
	void deleteByBookId( Long id );
	
	@Transactional
	@Modifying
    @Query( value="update books set title = :title, author = :author, borrower = :borrower, status = :status where id = :id", nativeQuery = true)
    void updateBook(@Param("id") String id, @Param("title") String title, @Param("author") String author, @Param("borrower") String borrower, @Param("status") String status );

	@Transactional
	@Modifying
    @Query( value="update books set status = :status, borrower = :borrower where id = :id", nativeQuery = true)
	void updateBookByState(@Param("id") long id, @Param("status") String status, @Param("borrower") String borrower);

	List<Book> findByBorrower(String username);
	   
}
