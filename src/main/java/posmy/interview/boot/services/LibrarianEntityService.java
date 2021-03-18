package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.repository.LibrarianRepository;

@Service
public class LibrarianEntityService extends BaseEntityService<Librarian, Long> {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    protected PagingAndSortingRepository<Librarian, Long> getRepository() {
        return librarianRepository;
    }

    @Transactional
    public Librarian create(Librarian librarian) {
        return super.create(librarian);
    }

    @Transactional
    public Librarian update(Librarian librarian) {
        return super.update(librarian);
    }

    @Override
    protected void doCreate(Librarian entity) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected Librarian doUpdate(Librarian entity, Librarian changeSet) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void doDelete(Librarian entity) {
        throw new UnsupportedOperationException("Not supported");
    }
}
