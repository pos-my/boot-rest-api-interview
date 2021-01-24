package posmy.interview.boot.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Qualifier("books")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
