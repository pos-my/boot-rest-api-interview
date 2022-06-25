package posmy.interview.boot.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.model.Librarian;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface LibrarianRepository extends JpaRepository<Librarian, UUID> {

	Librarian findByLibrarianId(String librarianId);
	Librarian deleteByLibrarianId(String librarianId) throws Exception;
}
