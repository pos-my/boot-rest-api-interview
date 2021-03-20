package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.repository.LibrarianRepository;

@Service
public class LibrarianService extends BaseEntityService<Librarian, Long> {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    protected PagingAndSortingRepository<Librarian, Long> getRepository() {
        return librarianRepository;
    }

    @PreAuthorize("hasAuthority('LIBRARIAN_CREATE')")
    @Transactional
    public Librarian create(Librarian librarian) {
        return super.create(librarian);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN_UPDATE')")
    @Transactional
    public Librarian update(Long id, Librarian librarian) {
        librarian.setId(id);
        return super.update(librarian);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN_DELETE')")
    @Transactional
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected void doCreate(Librarian entity) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void doUpdate(Librarian entity, Librarian changeSet) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void doDelete(Librarian entity) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Transactional(readOnly = true)
    public Librarian getByToken(String token) {
        return librarianRepository.findByToken(token);
    }
}
