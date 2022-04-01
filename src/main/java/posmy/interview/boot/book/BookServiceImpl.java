package posmy.interview.boot.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;

import posmy.interview.boot.book.requests.NewBookRequest;
import posmy.interview.boot.book.requests.UpdateBookRequest;
import posmy.interview.boot.common.enums.BookStatusEnum;
import posmy.interview.boot.entities.Book;
import posmy.interview.boot.entities.User;
import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.ExceptionConstants;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.repositories.BookRepository;
import posmy.interview.boot.repositories.UserRepository;
import posmy.interview.boot.security.BaseSecurityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class BookServiceImpl extends BaseSecurityService implements BookService {
    private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

    @Autowired
    @NotNull
    private BookRepository bookRepo;

    @Autowired
    @NotNull
    private UserRepository userRepo;

    @Override
    public void delete(Long id) throws RecordNotFoundException {
        Book book = bookRepo.getById(id);

        if (book == null) {
            throw new RecordNotFoundException(ExceptionConstants.BOOK_NOT_FOUND_EXCEPTION);
        }
        bookRepo.delete(this.view(id));
    }

    @Override
    public List<Book> viewAll() {
        List<Book> books = new ArrayList<>();
        if (this.isLibrarian()) {
            books = bookRepo.findAll();
        } else {
            books = bookRepo.findByStatus(BookStatusEnum.AVAILABLE.toString());
        }
        logger.info("View all:{}", books);
        return books;
    }

    @Override
    public Book view(Long id) throws RecordNotFoundException {

        logger.info("View id:{}", id);
        Optional<Book> book = bookRepo.findById(id);

        if (book.isEmpty()) {
            throw new RecordNotFoundException(ExceptionConstants.BOOK_NOT_FOUND_EXCEPTION);
        }
        if ((this.isLibrarian())
                || (StringUtils.equals(book.get().getStatus().toUpperCase(), BookStatusEnum.AVAILABLE.toString()))) {
            return book.get();
        }
        return new Book();
    }

    @Override
    public Book add(NewBookRequest bookRequest) throws DuplicateRecordException {
        logger.info("Book add id:{}", bookRequest);

        Book book = this.modelMapper.map(bookRequest, Book.class);
        logger.info("Book add id:{}", book);

        logger.info("Add Book {}", book);
        if (bookRepo.findByIsnb(bookRequest.getIsnb()) != null) {
            throw new DuplicateRecordException(ExceptionConstants.BOOK_IS_EXISTS);
        }
        book.setUser(null);
        book.setStatus(BookStatusEnum.AVAILABLE.toString());
        bookRepo.saveAndFlush(book);
        return book;
    }

    @Override
    public Book update(Long id, UpdateBookRequest bookRequest) throws RecordNotFoundException {
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isEmpty()) {
            throw new RecordNotFoundException(ExceptionConstants.BOOK_NOT_FOUND_EXCEPTION);
        }
        Book book = bookOptional.get();
        if (this.isLibrarian()) {
            book.setAuthor(bookRequest.getTitle());
            book.setStatus(bookRequest.getStatus().toUpperCase());
            book.setTitle(bookRequest.getTitle());

            if (StringUtils.equals(bookRequest.getStatus().toUpperCase(), BookStatusEnum.BORROWED.toString())) {
                User user = userRepo.findByUsername(bookRequest.getUser());
                book.setUser(user);
            } else {
                book.setUser(null);
            }
        }

        bookRepo.saveAndFlush(book);

        return book;
    }
}