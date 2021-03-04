package posmy.interview.boot.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.model.request.BookGetRequest;
import posmy.interview.boot.model.response.BookGetResponse;
import posmy.interview.boot.repos.BookRepository;

@Service
public class BookGetService implements BaseService<BookGetRequest, BookGetResponse> {

    private final BookRepository bookRepository;

    public BookGetService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookGetResponse execute(BookGetRequest request) {
        Page<Book> page = bookRepository.findAll(request.getPageable());
        return BookGetResponse.builder()
                .page(page)
                .build();
    }
}
