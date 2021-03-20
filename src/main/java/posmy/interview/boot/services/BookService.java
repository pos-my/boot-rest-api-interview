package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.repository.BookRepository;

@Service
public class BookService extends BaseEntityService<Book, Long> {

    @Autowired
    private BookRepository bookRepository;

    @Override
    protected PagingAndSortingRepository<Book, Long> getRepository() {
        return bookRepository;
    }

    @PreAuthorize("hasAuthority('BOOK_READ')")
    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return super.getById(id);
    }

    @PreAuthorize("hasAuthority('BOOK_CREATE')")
    @Transactional
    public Book create(Book book) {
        return super.create(book);
    }

    @PreAuthorize("hasAuthority('BOOK_UPDATE')")
    @Transactional
    public Book update(Long id, Book book) {
        book.setId(id);
        return super.update(book);
    }

    @PreAuthorize("hasAuthority('BOOK_DELETE')")
    @Transactional
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected void doCreate(Book entity) {
        failIfBlankOrNull("title", entity.getTitle());
        entity.setStatus(Book.Status.AVAILABLE);
    }

    @Override
    protected void doUpdate(Book entity, Book changeSet) {

        if (isChanged(entity.getTitle(), changeSet.getTitle())) {
            entity.setTitle(changeSet.getTitle());
        }

    }

    @Override
    protected void doDelete(Book entity) {
    }

    @PreAuthorize("hasAuthority('BOOK_READ')")
    @Transactional(readOnly = true)
    public Page<Book> get(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('BOOK_UPDATE') or hasAuthority('BOOK_BORROW')")
    @Transactional
    public Book borrowBook(Long id) {
        Book pBook = this.getById(id);
        if (Book.Status.AVAILABLE.equals(pBook.getStatus())) {
            // we should le this go through generic update method!
            pBook.setStatus(Book.Status.BORROWED);
            return bookRepository.save(pBook);
        } else {
            // we should not throw exception here
            throw new IllegalStateException(String.format("Book %s already borrowed", pBook.getTitle()));
        }
    }

    @PreAuthorize("hasAuthority('BOOK_UPDATE') or hasAuthority('BOOK_RETURN')")
    @Transactional
    public Book returnBook(Long id) {
        Book pBook = this.getById(id);
        if (Book.Status.BORROWED.equals(pBook.getStatus())) {
            // we should le this go through generic update method!
            pBook.setStatus(Book.Status.AVAILABLE);
            return bookRepository.save(pBook);
        } else {
            // we should not throw exception here
            throw new IllegalStateException(String.format("Book %s already in shelves", pBook.getTitle()));
        }
    }
}
