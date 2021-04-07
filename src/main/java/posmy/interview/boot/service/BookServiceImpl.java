package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.exception.BookNotFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.system.BookMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

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
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookMapper::convertToDto).orElse(null);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.convertToModel(bookDto);
        Book createdBook = bookRepository.save(book);
        return bookMapper.convertToDto(createdBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, String id) {
        Book book = bookRepository.findById(id)
                .map(bk -> {
                    bk.setName(bookDto.getName());
                    bk.setStatus(bookDto.getStatus());
                    return bk;
                })
                .orElseThrow(() -> new BookNotFoundException(id));
        Book updatedBook = bookRepository.save(book);
        return bookMapper.convertToDto(updatedBook);
    }

    @Override
    public void deleteBook(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }
}
