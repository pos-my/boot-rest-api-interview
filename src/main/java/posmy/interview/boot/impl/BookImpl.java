package posmy.interview.boot.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import posmy.interview.boot.constant.Constant;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repo.BookRepository;
import posmy.interview.boot.repo.UserRepository;
import posmy.interview.boot.service.BookService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BookImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Book addBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (optionalBook.isPresent()) {
            log.error(Constant.BOOK_ERROR_BOOK_EXIST);
            throw new EntityExistsException(Constant.BOOK_ERROR_BOOK_EXIST);
        }

        Date date = new Date();
        book.setStatus(Constant.BookStatus.AVAILABLE.name());
        book.setDateCreated(date);
        book.setDateUpdated(date);

        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (!optionalBook.isPresent()) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new EntityNotFoundException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        Book existingBook = optionalBook.get();
        existingBook.setDateUpdated(new Date());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setTitle(book.getTitle());
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (!optionalBook.isPresent()) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new EntityNotFoundException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        Book existingBook = optionalBook.get();
        if (Constant.BookStatus.BORROWED.name().equals(existingBook.getStatus())) {
            log.error(Constant.BOOK_ERROR_BOOK_BORROWED);
            throw new RuntimeException(Constant.BOOK_ERROR_BOOK_BORROWED);
        }

        bookRepository.deleteById(existingBook.getId());
    }

    @Override
    public Page<Book> getBooks(int page) {
        return bookRepository.findAll(PageRequest.of(page, Constant.PAGE_INDEX));
    }

    @Override
    public Book borrowBook(Book book, String username) {
        if (null == book) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new NullPointerException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        if (null == username || "".equals(username)) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new NullPointerException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalBook.isPresent()) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new EntityNotFoundException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        Book existingBook = optionalBook.get();
        if (Constant.BookStatus.BORROWED.name().equals(existingBook.getStatus())) {
            log.error(Constant.BOOK_ERROR_BOOK_IS_BORROWED);
            throw new EntityNotFoundException(Constant.BOOK_ERROR_BOOK_IS_BORROWED);
        }

        existingBook.setDateUpdated(new Date());
        existingBook.setBorrower(username);
        existingBook.setStatus(Constant.BookStatus.BORROWED.name());
        return bookRepository.save(existingBook);
    }

    @Override
    public Book returnBook(Book book, String username) {
        if (null == book) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new NullPointerException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        if (null == username || "".equals(username)) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new NullPointerException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalBook.isPresent()) {
            log.error(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
            throw new EntityNotFoundException(Constant.BOOK_ERROR_BOOK_NOT_EXIST);
        }

        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        Book existingBook = optionalBook.get();
        if (Constant.BookStatus.AVAILABLE.name().equals(existingBook.getStatus())) {
            log.error(Constant.BOOK_ERROR_BOOK_IS_NOT_BORROWED);
            throw new RuntimeException(Constant.BOOK_ERROR_BOOK_IS_NOT_BORROWED);
        }

        if (!existingBook.getBorrower().equals(username)) {
            log.error(Constant.BOOK_ERROR_BORROWER_DOES_NOT_MATCH);
            throw new RuntimeException(Constant.BOOK_ERROR_BORROWER_DOES_NOT_MATCH);
        }

        existingBook.setDateUpdated(new Date());
        existingBook.setBorrower(null);
        existingBook.setStatus(Constant.BookStatus.AVAILABLE.name());
        return bookRepository.save(existingBook);
    }
}
