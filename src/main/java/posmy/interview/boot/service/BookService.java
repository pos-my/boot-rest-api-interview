package posmy.interview.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dto.CreateBookRecordDTO;
import posmy.interview.boot.dto.UpdateBookRecordDTO;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.NotFoundException;
import posmy.interview.boot.repository.BookRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllAvailableBooks() {
        return bookRepository.findAllByBookStatusIs(BookStatus.AVAILABLE);
    }

    @Transactional
    public void createNewBookRecord(CreateBookRecordDTO createBookRecordDTO) {
        bookRepository.save(Book.builder()
                .title(createBookRecordDTO.getTitle())
                .description(createBookRecordDTO.getDescription())
                .author(createBookRecordDTO.getAuthor())
                .build());
    }

    @Transactional
    public Book updateBookStatus(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        if (BookStatus.AVAILABLE.equals(book.getBookStatus())) {
            book.setBookStatus(BookStatus.BORROWED);
        } else {
            book.setBookStatus(BookStatus.AVAILABLE);
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateNewBookRecord(Long bookId, UpdateBookRecordDTO updateBookRecordDTO) {
        Book updateBook = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        updateBook.setTitle(updateBookRecordDTO.getTitle());
        updateBook.setDescription(updateBookRecordDTO.getDescription());
        updateBook.setAuthor(updateBookRecordDTO.getAuthor());
        updateBook.setBookStatus(updateBookRecordDTO.getBookStatus());
        return bookRepository.save(updateBook);
    }

    @Transactional
    public void deleteBookRecord(Long bookId) {
        Book deleteBook = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        bookRepository.delete(deleteBook);
    }
}
