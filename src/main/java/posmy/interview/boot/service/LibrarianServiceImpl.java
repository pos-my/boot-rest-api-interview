package posmy.interview.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.model.Librarian;
import posmy.interview.boot.repository.LibrarianRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class LibrarianServiceImpl implements LibrarianService {

    @Autowired
	private LibrarianRepository librarianRepo;

	@Override
	public List<Librarian> findAllLibrarians() {
		return (List<Librarian>) librarianRepo.findAll();
	}

	@Override
	public Librarian findByLibrarianId(String librarianId) {
		// TODO Auto-generated method stub
		return librarianRepo.findByLibrarianId(librarianId);
	}

	@Override
	public Librarian saveLibrarian(Librarian Librarian) throws Exception {
		return librarianRepo.save(Librarian);
	}

	@Override
	public Librarian updateLibrarian(Librarian Librarian) throws Exception {
		// TODO Auto-generated method stub
		return saveLibrarian(Librarian);
	}

	@Override
	public Librarian deleteByLibrarianId(String librarianId) throws Exception {
		// TODO Auto-generated method stub
		return librarianRepo.deleteByLibrarianId(librarianId);
	}

	@Override
	public void deleteLibrarian(Librarian Librarian) throws Exception {
		// TODO Auto-generated method stub
		librarianRepo.delete(Librarian);
	}
}
