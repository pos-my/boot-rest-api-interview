package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Books;
import posmy.interview.boot.repository.BooksRepository;

import java.util.List;

@RestController
@RequestMapping("/bookManagement")
public class BookManagementController {

    @Autowired
    private BooksRepository booksRepository;

    @PostMapping("/book")
    public Books addBook(@RequestBody Books books){
        return booksRepository.save(books);
    }

    @GetMapping("/book/{id}")
    public Books getBook(@RequestParam("id") Long id){
        return booksRepository.getReferenceById(id);
    }

    @GetMapping("/book")
    public List<Books> getAllBook(){
        return booksRepository.findAll();
    }

    @PutMapping("/book")
    public Books updateBook(@RequestBody Books books){
        return booksRepository.save(books);
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@RequestParam("id") Long id){
        booksRepository.deleteById(id);
    }
}
