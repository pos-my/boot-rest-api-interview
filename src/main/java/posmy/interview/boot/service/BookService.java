package posmy.interview.boot.service;

import posmy.interview.boot.model.BookResponse;

public interface BookService {
    BookResponse processBookQueryRequest(int pageSize, int pageNumber,
                                         String name, String description,
                                         String status);
}
