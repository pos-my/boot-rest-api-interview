package posmy.interview.boot.service;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.request.CreateBookDto;
import posmy.interview.boot.dto.request.SearchBookDto;
import posmy.interview.boot.dto.request.UpdateBookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.NotFoundException;
import posmy.interview.boot.exception.ValidationException;
import posmy.interview.boot.mapper.BookMapper;
import posmy.interview.boot.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.getInstance();

    public Page<Book> getBooks(SearchBookDto searchBookDto, Pageable pageable) {
        String title = searchBookDto.getTitle();

        Specification<Book> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isNotBlank(title)) {
                predicates.add(
                    criteriaBuilder.like(root.get("title"), "%" + title + "%")
                );
            }

            predicates.add(
                criteriaBuilder.equal(root.get("status"), BookStatus.AVAILABLE)
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Book> bookPage = bookRepository.findAll(specification, pageable);
        List<Book> bookList = new ArrayList<>();

        bookPage.forEach(bookList::add);
        return new PageImpl<>(bookList, pageable, bookPage.getTotalElements());
    }

    @Transactional
    public Book createBook(CreateBookDto createBookDto) {
        Book book = bookMapper.mapBookDtoToEntity(createBookDto);
        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, UpdateBookDto updateBookDto) {
        Book book = findBookBy(id);
        book.setTitle(updateBookDto.getTitle());
        book.setAuthor(updateBookDto.getAuthor());
        book.setStatus(updateBookDto.getStatus());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookBy(id);
        bookRepository.delete(book);
    }

    @Transactional
    public Book borrowBook(Long id) {
        Book book = findBookBy(id);

        if (validateBookStatus(book.getStatus())) {
            book.setStatus(BookStatus.BORROWED);
            return bookRepository.save(book);
        }

        throw new ValidationException("Book is not available to borrow");
    }

    @Transactional
    public Book returnBook(Long id) {
        Book book = findBookBy(id);

        if (validateBookStatus(book.getStatus())) {
            throw new ValidationException("Book is already returned");
        }

        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    private Book findBookBy(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found for id: " + id));
    }

    private boolean validateBookStatus(BookStatus status) {
        return BookStatus.AVAILABLE.equals(status);
    }
}
