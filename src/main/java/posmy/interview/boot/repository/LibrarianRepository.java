package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Librarian;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
}
