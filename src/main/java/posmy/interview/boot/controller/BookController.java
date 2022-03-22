package posmy.interview.boot.controller;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.response.BookResponse;
import posmy.interview.boot.services.book.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/viewAll")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<List<BookResponse>> viewAll() {
        return ResponseEntity.ok(this.bookService.viewAll());
    }

    @GetMapping("/view")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> view(@RequestParam String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.bookService.view(id));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
        }
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> delete(@RequestParam String id){

        try{
            bookService.remove(id);
            return ResponseEntity.ok().body("Book with id = " + id + " is removed.");
        }catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
        }

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<BookResponse> add(@RequestBody BookRequest bookRequest){
        return ResponseEntity.ok(this.bookService.save(bookRequest));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> update(@RequestBody BookRequest bookRequest){
        try{
            return ResponseEntity.ok(this.bookService.update(bookRequest));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
        }
    }

    @PutMapping("/borrow")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> borrow(@RequestParam String id){
        try{
            return ResponseEntity.ok(this.bookService.borrow(id));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
        }
    }

    @PutMapping("/return")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> returnBook(@RequestParam String id){
        try{
            return ResponseEntity.ok(this.bookService.returnBook(id));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
        }
    }
}
