package posmy.interview.boot.controller;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.exception.InvalidPaginationException;
import posmy.interview.boot.model.BookResponse;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.util.PaginationUtil;

@RestController
public class BookController implements BookOperations {

    Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @Override
    public BookResponse getBooks(Integer pageSize, Integer pageNumber,
                                 String name, String description,
                                 String status) {

        try {
            PaginationUtil.validatePagination(pageNumber, pageSize);
            BookResponse bookResponse =
                    bookService.processBookQueryRequest(pageSize, pageNumber, name, description, status);
            return bookResponse;
        } catch (InvalidPaginationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page number or size");
        }
    }

}
