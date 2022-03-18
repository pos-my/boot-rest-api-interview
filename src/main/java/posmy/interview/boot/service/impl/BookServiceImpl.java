package posmy.interview.boot.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import posmy.interview.boot.db.BookRepository;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.BookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookRepository.findAll().forEach(bookList::add);
        return bookList;
    }

    @Override
    public List<Book> addBooks(List<String> titleList) throws Exception {
        if (CollectionUtils.isNotEmpty(titleList)) {
            List<Book> existingBookList = bookRepository.findByTitleIn(titleList);
            if (CollectionUtils.isNotEmpty(existingBookList)) {
                throw new Exception("Book already exist");
            }
            
            List<Book> bookList = titleList.stream().map( title -> {
                return Book.builder()
                        .title(title)
                        .status(BookStatus.AVAILABLE).build();
            }).collect(Collectors.toList());
            return (List<Book>) bookRepository.saveAll(bookList);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> updateBooks(List<Book> bookList) {
        return (List<Book>) bookRepository.saveAll(bookList);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book borrowBook(Long id) throws Exception {
        Optional<Book> requestBook = bookRepository.findById(id);
        if (requestBook.isPresent()) {
            Book book = requestBook.get();
            if (BookStatus.BORROWED.equals(book.getStatus())) {
                throw new Exception("The book has been borrowed");
            }
            book.setStatus(BookStatus.BORROWED);
            bookRepository.save(book);
            return book;
        }
        return null;
    }

    @Override
    public Book returnBook(Long id) throws Exception {
        Optional<Book> requestBook = bookRepository.findById(id);
        if (requestBook.isPresent()) {
            Book book = requestBook.get();
            if (BookStatus.AVAILABLE.equals(book.getStatus())) {
                throw new Exception("The book has not been borrowed");
            }
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);
            return book;
        }
        return null;
    }
}
