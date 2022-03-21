package posmy.interview.boot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repo.AppUserRepo;
import posmy.interview.boot.repo.BookRepo;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService{
    private final AppUserRepo appUserRepo;
    private final BookRepo bookRepo;

    @Override
    public Book borrowBook(String username, String isbn) throws UsernameNotFoundException, ValidationException {
        AppUser user = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        Book book = bookRepo.findByIsbn(isbn)
                .orElseThrow(() -> new ValidationException("Book not exist"));

        if("AVAILABLE".equals(book.getStatus())){
            log.info("Set borrower {} for book {}", user.getUsername(), book.getTitle());
            log.info("Set book status to BORROWED");

            book.setBorrower(user);
            book.setStatus("BORROWED");
            return bookRepo.save(book);
        } else {
            throw new ValidationException("Book is unavailable");
        }
    }

    @Override
    public Book returnBook(String username, String isbn) throws ValidationException {
        AppUser user = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        Book book = bookRepo.findByIsbn(isbn)
                .orElseThrow(() -> new ValidationException("Book not exist"));

        if("BORROWED".equals(book.getStatus())){
            if(Objects.equals(book.getBorrower().getId(), user.getId())){
                log.info("Set book status to AVAILABLE");

                book.setBorrower(null);
                book.setStatus("AVAILABLE");
                return bookRepo.save(book);
            } else {
                throw new ValidationException(String.format(
                        "Borrower id mismatch, user[%s] but borrower[%s]", user.getUsername(), book.getBorrower().getUsername()
                ));
            }
        } else {
            throw new ValidationException("This book is not borrowed");
        }
    }

    @Override
    public Book getBook(String isbn) throws ValidationException {
        log.info("Fetching book with isbn {}", isbn);
        Optional<Book> book = bookRepo.findByIsbn(isbn);
        return book.orElseThrow(() -> new ValidationException("No book find with isbn " + isbn));
    }

    @Override
    public Book saveBook(Book book) {
        if(book.getId() != null){
            log.info("Updating book {}", book);
        } else {
            log.info("Adding book {}", book);
        }
        return bookRepo.save(book);
    }

    @Override
    public void removeBook(String isbn) {
        log.info("Removing book with isbn {}", isbn);
        bookRepo.deleteByIsbn(isbn);
    }

    @Override
    public List<Book> getBooks() {
        log.info("Fetching all books");
        return bookRepo.findAll();
    }
}
