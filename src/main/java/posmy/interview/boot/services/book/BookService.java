package posmy.interview.boot.services.book;

import posmy.interview.boot.model.entity.BookEntity;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.response.BookResponse;

import java.util.List;

public interface BookService {

    //Librarians
    void remove(String id);
    List<BookResponse> viewAll();
    BookResponse view(String id);
    BookResponse save(BookRequest bookRequest);
    BookResponse update(BookRequest bookRequest);

    //Members
    BookResponse borrow(String id);
    BookResponse returnBook(String id);
}
