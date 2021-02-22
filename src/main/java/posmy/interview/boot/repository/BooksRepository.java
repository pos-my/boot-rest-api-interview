package posmy.interview.boot.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.model.Books;

import java.awt.print.Book;

@Repository
public interface BooksRepository extends CrudRepository<Books, Integer> {

     @Modifying
     @Query("update Books u set u.status = 'BORROWED' where u.book_id = :bookid")
     void borrowBooks(@Param("bookid") Long bookid);

     @Modifying
     @Query("update Books u set u.status = 'AVAILABLE' where u.book_id = :bookid")
     void returnBook(@Param("bookid") Long bookid) ;

}
