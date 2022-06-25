package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.model.Librarian;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface LibrarianService {

    List<Librarian> findAllLibrarians();
    Librarian findByLibrarianId(String librarianId);
	Librarian saveLibrarian(Librarian Librarian) throws Exception;
	Librarian updateLibrarian(Librarian Librarian) throws Exception;
	Librarian deleteByLibrarianId(String librarianId) throws Exception;
	void deleteLibrarian(Librarian Librarian) throws Exception;
}
