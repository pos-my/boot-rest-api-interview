package posmy.interview.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.domains.Books;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByBookName(String bookName);

    long deleteByBookName(String bookName);

    @Modifying
    @Query("update Books b set b.status = :status where b.bookName = :bookname")
    void updateStatusByBookName(@Param("status") Enum status, @Param("bookname") String bookname);

    @Modifying
    @Query("update Books b set b.bookName = :newname where b.bookName = :bookname")
    void updateBookByBookName(@Param("newname") String newname, @Param("bookname") String bookname);
}
