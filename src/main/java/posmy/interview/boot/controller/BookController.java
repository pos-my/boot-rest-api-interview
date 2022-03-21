package posmy.interview.boot.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.MessageResponse;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    private final BookService bookService;
    private final String secret;
    private final Gson gson;

    @Autowired
    public BookController(BookService bookService,
                          @Value("${security.authentication.secret}") String secret,
                          Gson gson) {
        this.bookService = bookService;
        this.secret = secret;
        this.gson = gson;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getBook(@RequestParam String isbn){
        try{
            return ResponseEntity.ok().body(bookService.getBook(isbn));

        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(PRECONDITION_FAILED).contentType(APPLICATION_JSON).body(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    )
            );
        }
    }

    @GetMapping("/get-all-books")
    public ResponseEntity<List<Book>> getBooks(){
        return ResponseEntity.ok().body(bookService.getBooks());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBook(@RequestBody Book book){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/book/save").toUriString());
        return ResponseEntity.created(uri).contentType(APPLICATION_JSON).body(
                gson.toJson(
                        bookService.saveBook(book)
                )
        );
    }

    @DeleteMapping("/remove")
    public void removeBook(@RequestParam String isbn){
        bookService.removeBook(isbn);
    }

    @PostMapping("/borrow")
    public void borrowBook(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        try{
            String isbn = request.getParameter("isbn");
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            DecodedJWT decodedJWT = TokenUtil.verifyToken(authorizationHeader, secret);
            if(decodedJWT != null){
                String username = decodedJWT.getSubject();
                Book book = bookService.borrowBook(username, isbn);

                response.setContentType(APPLICATION_JSON_VALUE);
                response.getOutputStream().write(
                        gson.toJson(
                                MessageResponse.builder()
                                        .status(OK.value())
                                        .message("Book borrowed successfully, title["+ book.getTitle()+"]")
                                        .build()
                        ).getBytes());
            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(PRECONDITION_FAILED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    ).getBytes());
        }
    }

    @PostMapping("/return")
    public void returnBook(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        try{
            String isbn = request.getParameter("isbn");
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            DecodedJWT decodedJWT = TokenUtil.verifyToken(authorizationHeader, secret);
            if(decodedJWT != null){
                String username = decodedJWT.getSubject();
                Book book = bookService.returnBook(username, isbn);

                response.setContentType(APPLICATION_JSON_VALUE);
                response.getOutputStream().write(
                        gson.toJson(
                                MessageResponse.builder()
                                        .status(OK.value())
                                        .message("Book returned successfully, title["+ book.getTitle()+"]")
                                        .build()
                        ).getBytes());
            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(PRECONDITION_FAILED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    ).getBytes());
        }
    }
}
