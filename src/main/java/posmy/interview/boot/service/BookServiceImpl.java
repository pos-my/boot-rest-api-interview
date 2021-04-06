package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.system.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
}
