package posmy.interview.boot.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.exception.*;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.system.BookMapper;
import posmy.interview.boot.system.Constant;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Setter
@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream()
                .map(bookMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findById(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.convertToDto(book);
    }

    @Override
    public List<BookDto> findAvailableBooks() {
        List<Book> availableBooks = bookRepository.findByStatus(Constant.BookState.AVAILABLE);
        return availableBooks.stream()
                .map(bookMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.convertToModel(bookDto);
        Book createdBook = bookRepository.save(book);
        return bookMapper.convertToDto(createdBook);
    }

    // This is only for updating book name
    // Status update will be handled by updateBookStatusForBorrow() and updateBookStatusForReturn()
    @Override
    public BookDto updateBook(BookDto bookDto, String id) {
        Book book = bookRepository.findById(id)
                .map(bk -> {
                    if (bookDto.getName() != null) {
                        bk.setName(bookDto.getName());
                    }
                    return bk;
                })
                .orElseThrow(() -> new BookNotFoundException(id));
        Book updatedBook = bookRepository.save(book);
        return bookMapper.convertToDto(updatedBook);
    }

    @Override
    public void deleteBook(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    @Override
    public BookDto borrowBook(String loginId, String bookId) {
        User member = userRepository.findFirstByLoginId(loginId)
                .orElseThrow(() -> new UserNotFoundException(loginId));
        return updateBookStatus(member, bookId, Constant.BookState.BORROWED,
                () -> new BookAlreadyBorrowedException(bookId));
    }

    @Override
    public BookDto returnBook(String bookId) {
        return updateBookStatus(null, bookId, Constant.BookState.AVAILABLE,
                () -> new BookAlreadyReturnedException(bookId));
    }

    private BookDto updateBookStatus(User member, String bookId, Constant.BookState status,
                                     Supplier<? extends BaseRuntimeException> exceptionSupp) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (book.getStatus() == status) {
            throw exceptionSupp.get();
        }
        book.setStatus(status);
        book.setUser(member);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.convertToDto(updatedBook);
    }

}
