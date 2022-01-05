package posmy.interview.boot.controller;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.exception.InvalidArgumentException;
import posmy.interview.boot.exception.InvalidPaginationException;
import posmy.interview.boot.exception.UnauthorisedException;
import posmy.interview.boot.model.book.*;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.util.Json;
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

    @Override
    public BookCreatedResponse addBooks(CreateBookRequest createBookRequest) {
        try {
            logger.info("Processing add books api request, request = {}", Json.toString(createBookRequest));
            validateCreateBookRequest(createBookRequest);
            BookCreatedResponse bookCreatedResponse = bookService.createBook(createBookRequest.getName(), createBookRequest.getDescription(), createBookRequest.getStatus());
            logger.info("Successfully complete books api request, output = {}", Json.toString(bookCreatedResponse));
            return bookCreatedResponse;
        } catch (InvalidArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        }
    }

    @Override
    public UpdateBookResponse updateBooks(UpdateBookRequest updateBookRequest) {
        try {
            logger.info("Processing update books api request, request = {}", Json.toString(updateBookRequest));
            validateUpdateBookRequest(updateBookRequest);
            UpdateBookResponse updateBookResponse = bookService.updateBook(updateBookRequest.getId(), updateBookRequest.getName(), updateBookRequest.getDescription(), updateBookRequest.getStatus());
            logger.info("Successfully complete update books api request, output = {}", Json.toString(updateBookResponse));
            return updateBookResponse;
        } catch (InvalidArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        }  catch (UnauthorisedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorised access");
        }
    }


    public void validateCreateBookRequest(CreateBookRequest createBookRequest) throws InvalidArgumentException{
        if (createBookRequest.getName() == null || createBookRequest.getName().length() == 0 || createBookRequest.getName().replace(" ", "").equals("")){
            throw new InvalidArgumentException();
        }

        if (createBookRequest.getDescription() == null || createBookRequest.getDescription().length() == 0 || createBookRequest.getDescription().replace(" ", "").equals("")){
            throw new InvalidArgumentException();
        }

        if (createBookRequest.getStatus() == null || createBookRequest.getStatus().length() == 0 || createBookRequest.getStatus().replace(" ", "").equals("")){
            throw new InvalidArgumentException();
        }
    }

    public void validateUpdateBookRequest(UpdateBookRequest updateBookRequest) throws InvalidArgumentException{
        if (updateBookRequest.getId() == null || updateBookRequest.getId() < 0){
            throw new InvalidArgumentException();
        }
    }
}
