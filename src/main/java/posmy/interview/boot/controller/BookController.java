package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(bookService.findAll());
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "title") String title) {
        return ResponseEntity.ok(bookService.update(id, title));
    }
}
