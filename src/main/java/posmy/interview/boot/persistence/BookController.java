package posmy.interview.boot.persistence;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.errorHandling.BookNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class BookController {

    private final BookRepository repository;

    private final BookModelAssembler assembler;

    BookController(@Qualifier("books") BookRepository repository, BookModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Read all books
    @GetMapping("/books")
    CollectionModel<EntityModel<Book>> all() {
        List<EntityModel<Book>> books = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(books,
                linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    // Read book
    @GetMapping("/books/{id}")
    EntityModel<Book> one(@PathVariable Long id) {
        return assembler.toModel(repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id)));
    }

    // Create new book
    @PostMapping("/books")
    ResponseEntity<?> newBook(@RequestBody Book newBook) {
        EntityModel<Book> entityModel = assembler.toModel(repository.save(newBook));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Update/Replace book
    @PutMapping("/books/{id}")
    ResponseEntity<?> replaceBook(@RequestBody Book newBook, @PathVariable Long id) {

        Book updatedBook = repository.findById(id) //
                .map(book -> {
                    book.setName(newBook.getName());
                    book.setStatus(newBook.getStatus());
                    return repository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });

        EntityModel<Book> entityModel = assembler.toModel(updatedBook);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @PutMapping(path = "/books/{id}/borrow")
    ResponseEntity<?> borrowBook(@PathVariable Long id) {
        final Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.getStatus() != Status.AVAILABLE) {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method not allowed")
                            .withDetail("You cant borrow a book that is in the " +
                                    book.getStatus() + " status"));
        }

        book.setStatus(Status.BORROWED);
        return ResponseEntity.ok(assembler.toModel(repository.save(book)));
    }

    @PutMapping(path = "/books/{id}/return")
    ResponseEntity<?> returnBook(@PathVariable Long id) {
        final Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.getStatus() != Status.BORROWED) {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method not allowed")
                            .withDetail("You cant return a book that is in the " +
                                    book.getStatus() + " status"));
        }

        book.setStatus(Status.AVAILABLE);
        return ResponseEntity.ok(assembler.toModel(repository.save(book)));
    }

    // Delete book
    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
