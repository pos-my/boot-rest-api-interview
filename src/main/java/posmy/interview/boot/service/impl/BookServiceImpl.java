package posmy.interview.boot.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> viewBooks() {
        List<Book> bookList = new ArrayList<>();
        this.bookRepository.findAll().forEach(bookList::add);
        return bookList;
    }

    @Override
    public List<Book> addBooks(List<Book> bookList) {
        return (List<Book>) this.bookRepository.saveAll(bookList);
    }

    @Override
    public List<Book> updateBooks(List<Book> bookList) {
        return (List<Book>) this.bookRepository.saveAll(bookList);
    }

    @Override
    public void removeBook(Integer bookId) {
        this.bookRepository.deleteById(bookId);
    }

    @Override
    public Book borrowBook(Integer bookId) {
        Optional<Book> requestBook = this.bookRepository.findById(bookId);
        if (requestBook.isPresent()) {
            Book book = requestBook.get();
            book.setBookStatus("BORROWED");
            this.bookRepository.save(book);
            return book;
        }
        throw new ObjectNotFoundException("Unable to find book with ID: {}", String.valueOf(bookId));
    }

    @Override
    public Book returnBook(Integer bookId) {
        Optional<Book> requestBook = this.bookRepository.findById(bookId);
        if (requestBook.isPresent()) {
            Book book = requestBook.get();
            book.setBookStatus("AVAILABLE");
            this.bookRepository.save(book);
            return book;
        }
        throw new ObjectNotFoundException("Unable to find book with ID: {}", String.valueOf(bookId));
    }


}
