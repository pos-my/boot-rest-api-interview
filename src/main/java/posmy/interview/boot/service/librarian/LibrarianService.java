package posmy.interview.boot.service.librarian;

import posmy.interview.boot.entity.LibrarianEntity;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateLibrarianRequest;
import posmy.interview.boot.model.Librarian;

public interface LibrarianService {
    Librarian getLibrarian(long libId) throws NoDataFoundException;
    LibrarianEntity saveLibrarian(CreateLibrarianRequest createLibrarianRequest);
}
