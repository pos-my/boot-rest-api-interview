package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.RestApiSuccessRepsonse;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookStatus;
import posmy.interview.boot.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book getBookById(Long bookId) throws CustomRestApiException {
        Optional<Book> bookData = bookRepository.findById(bookId);
        if (bookData.isEmpty()) {
            throw new CustomRestApiException("Book does not exist! BookID: " + bookId, HttpStatus.NOT_FOUND);
        }
        return bookData.get();
    }

    @Override
    public List<Book> getAllBooks() throws CustomRestApiException {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new CustomRestApiException("There are no books!", HttpStatus.NOT_FOUND);
        }
        return books;
    }

    @Override
    public RestApiSuccessRepsonse addBooks(List<Book> bookListRequest) throws CustomRestApiException {
        List<Book> booksToAdd = new ArrayList<>();
        for (Book book : bookListRequest) {
            Optional<Book> hasBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
            if (hasBook.isPresent()) {
                throw new CustomRestApiException("Book title {" + book.getTitle() + "} by " + book.getAuthor() +
                        " already exists!", HttpStatus.CONFLICT);
            }
            booksToAdd.add(book);
        }
        for (Book bookToAdd : booksToAdd) {
            bookRepository.save(bookToAdd);
        }

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully added book(s).");
    }

    @Override
    public RestApiSuccessRepsonse updateBookById(Long bookId, Book bookRequest) throws CustomRestApiException {
        Optional<Book> bookData = bookRepository.findById(bookId);
        if (bookData.isEmpty()) {
            throw new CustomRestApiException("Book does not exist! BookID: " + bookId, HttpStatus.NOT_FOUND);
        }
        Book updateBook = bookData.get();
        updateBook.setTitle(bookRequest.getTitle());
        updateBook.setAuthor(bookRequest.getAuthor());
        updateBook.setPublishedYear(bookRequest.getPublishedYear());
        updateBook.setStatus(bookRequest.getStatus());
        bookRepository.save(updateBook);

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully updated book.");
    }

    @Override
    public RestApiSuccessRepsonse removeBookById(Long bookId) throws CustomRestApiException {
        Optional<Book> bookData = bookRepository.findById(bookId);
        if (bookData.isEmpty()) {
            throw new CustomRestApiException("Book does not exist! BookID: " + bookId, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(bookId);

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully deleted book.");
    }

    @Override
    public RestApiSuccessRepsonse borrowBookById(Long bookId) throws CustomRestApiException {
        Optional<Book> bookData = bookRepository.findById(bookId);
        if (bookData.isEmpty()) {
            throw new CustomRestApiException("Book does not exist! BookID: " + bookId, HttpStatus.NOT_FOUND);
        }
        Book updateBook = bookData.get();
        if (updateBook.getStatus().equals(BookStatus.BORROWED)) {
            throw new CustomRestApiException("This book has been borrowed!", HttpStatus.CONFLICT);
        }
        updateBook.setStatus(BookStatus.BORROWED);
        bookRepository.save(updateBook);

        return new RestApiSuccessRepsonse(HttpStatus.OK,
                "Successfully borrowed book '" + updateBook.getTitle() + "' by " + updateBook.getAuthor());
    }

    @Override
    public RestApiSuccessRepsonse returnBookById(Long bookId) throws CustomRestApiException {
        Optional<Book> bookData = bookRepository.findById(bookId);
        if (bookData.isEmpty()) {
            throw new CustomRestApiException("Book does not exist! BookID: " + bookId, HttpStatus.NOT_FOUND);
        }
        Book updateBook = bookData.get();
        if (updateBook.getStatus().equals(BookStatus.AVAILABLE)) {
            throw new CustomRestApiException("This book has been returned!", HttpStatus.CONFLICT);
        }
        updateBook.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(updateBook);

        return new RestApiSuccessRepsonse(HttpStatus.OK,
                "Successfully returned book '" + updateBook.getTitle() + "' by " + updateBook.getAuthor());
    }
}
