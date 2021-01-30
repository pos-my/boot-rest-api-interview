package posmy.interview.boot.service.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.BookEntity;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.CreateBookRequest;
import posmy.interview.boot.model.UpdateBookRequest;
import posmy.interview.boot.repository.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookEntity createBook(CreateBookRequest createBookRequest) {
        BookEntity bookEntity = modelMapper.map(createBookRequest, BookEntity.class);
        bookEntity.setIsbn(UUID.randomUUID().toString());
        bookEntity.setStatus(BookStatus.AVAILABLE.name());
        bookEntity.setCreatedDate(new Date());
        bookEntity.setUpdatedDate(new Date());
        return bookRepository.save(bookEntity);
    }

    @Override
    public void deleteBook(long bookId) throws LmsException {
        BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
        if(bookEntity == null) {
            throw new LmsException("No book found for delete");
        }
        bookRepository.deleteById(bookId);
    }

    @Override
    public BookEntity updateBook(UpdateBookRequest updateBookRequest) throws LmsException {
        BookEntity bookEntity = bookRepository.findById(updateBookRequest.getBookId()).orElseThrow(() -> new LmsException("Book not found"));
        bookEntity.setTitle(updateBookRequest.getTitle());
        bookEntity.setAuthor(updateBookRequest.getAuthor());
        bookEntity.setUpdatedDate(new Date());
        return bookRepository.save(bookEntity);
    }

    @Override
    public Book getBook(long bookId) throws NoDataFoundException {
        log.info("Get book by id {}", bookId);
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new NoDataFoundException("There is no such book"));
        return modelMapper.map(bookEntity, Book.class);
    }

    @Override
    public List<Book> getAvailableBooks() throws LmsException {
        List<BookEntity> bookEntities = bookRepository.findByStatus(BookStatus.AVAILABLE.name()).orElseThrow(() -> new LmsException("There are no available books"));
        return bookEntities.stream().map(bookEntity -> modelMapper.map(bookEntity, Book.class)).collect(Collectors.toList());
    }

    @Override
    public void updateBookStatus(long bookId, BookStatus bookStatus) throws NoDataFoundException {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new NoDataFoundException("There is no such book"));
        bookEntity.setStatus(bookStatus.name());
        bookEntity.setUpdatedDate(new Date());
        bookRepository.save(bookEntity);
    }
}
