package posmy.interview.boot.manager.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import posmy.interview.boot.domain.Book;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.manager.BookManager;
import posmy.interview.boot.manager.MessageResources;
import posmy.interview.boot.repo.BookRepository;
import posmy.interview.boot.repo.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookManagerImpl implements BookManager {
    Logger logger = LoggerFactory.getLogger(BookManagerImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Book addBook(Book book) {
        Optional<Book> existingBook = bookRepository.findBookByIsbn(book.getIsbn());
        Book savedBook = null;

        if (existingBook.isPresent()) {
            throw new RuntimeException(MessageResources.BOOK_ALR_EXIST);
        } else {
            UUID uuid = UUID.randomUUID();
            Date date = new Date();
            book.setId(uuid);
            book.setCreatedDate(date);
            savedBook = bookRepository.save(book);
            logger.info("Book has been added: {}", savedBook.toString());
        }

        return savedBook;
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> existingBook = bookRepository.findById(book.getId());
        Book savedBook = null;

        if (existingBook.isPresent()) {
            Date date = new Date();
            Book currBook = existingBook.get();
            currBook.setTitle(book.getTitle());
            currBook.setAuthor(book.getAuthor());
            currBook.setBookStatus(book.getBookStatus());
            currBook.setBorrowedUser(book.getBorrowedUser());
            currBook.setUpdatedDate(date);
            savedBook = bookRepository.save(currBook);
            logger.info("Book has been updated: {}", savedBook.toString());
        } else {
            throw new EntityNotFoundException(MessageResources.UNABLE_UPDATE_BOOK_EXIST);
        }

        return savedBook;
    }

    @Override
    public void deleteBook(UUID bookId) {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            if (BookStatus.BORROWED.equals(book.getBookStatus())) {
                throw new RuntimeException(MessageResources.BOOK_BORROWED_UNABLE_DELETE);
            } else if (BookStatus.AVAILABLE.equals(book.getBookStatus())) {
                bookRepository.deleteById(bookId);
                logger.info("Book has been deleted: {}", book.toString());
            }
        } else {
            throw new EntityNotFoundException(MessageResources.UNABLE_DELETE_BOOK_NOT_EXIST);
        }
    }

    @Override
    public Page<Book> getAvailableBooksList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findBookByBookStatus(BookStatus.AVAILABLE, pageable);
    }

    @Override
    public Book borrowBook(UUID bookId, String username) {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        Optional<User> existingUser = userRepository.findUserByUsername(username);
        Book savedBook = null;

        if (existingBook.isPresent()) {
            if (existingUser.isPresent()) {
                Book book = existingBook.get();
                User user = existingUser.get();

                if (BookStatus.BORROWED.equals(book.getBookStatus())) {
                    throw new RuntimeException(MessageResources.BOOK_ALR_BORROWED);
                } else if (BookStatus.AVAILABLE.equals(book.getBookStatus())) {
                    Date date = new Date();
                    book.setBookStatus(BookStatus.BORROWED);
                    book.setBorrowedUser(user.getId());
                    book.setUpdatedDate(date);
                    savedBook = bookRepository.save(book);
                    logger.info("Book has been borrowed: {}", savedBook.toString());
                }
            } else {
                throw new EntityNotFoundException(MessageResources.USER_DOES_NOT_EXIST);
            }

        } else {
            throw new EntityNotFoundException(MessageResources.BOOK_DOES_NOT_EXIST);
        }

        return savedBook;
    }

    @Override
    public Book returnBook(UUID bookId) {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        Book savedBook = null;

        if (existingBook.isPresent()) {
            Book book = existingBook.get();

            if (BookStatus.BORROWED.equals(book.getBookStatus())) {
                Date date = new Date();
                book.setBookStatus(BookStatus.AVAILABLE);
                book.setBorrowedUser(null);
                book.setUpdatedDate(date);
                savedBook = bookRepository.save(book);
                logger.info("Book has been returned: {}", savedBook.toString());
            } else if (BookStatus.AVAILABLE.equals(book.getBookStatus())) {
                throw new RuntimeException(MessageResources.BOOK_NOT_BORROWED);
            }
        }

        return savedBook;
    }
}
