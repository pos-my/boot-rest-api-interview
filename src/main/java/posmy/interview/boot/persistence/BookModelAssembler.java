package posmy.interview.boot.persistence;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {

    @Override
    public EntityModel<Book> toModel(Book book) {

        // Unconditional links to single-item resource and aggregate root
        EntityModel<Book> bookModel = EntityModel.of(book,
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));

        // Conditional links based on state of the book
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final boolean isLibrarian = ((User) principal).getAuthorities().stream()
                .map(Objects::toString)
                .anyMatch(x -> x.equals(Role.LIBRARIAN.name()));

        if (!isLibrarian) {
            if (book.getStatus() == Status.AVAILABLE) {
                bookModel.add(linkTo(methodOn(BookController.class)
                        .borrowBook(book.getId())).withRel("borrow"));
            } else if (book.getStatus() == Status.BORROWED) {
                bookModel.add(linkTo(methodOn(BookController.class)
                        .returnBook(book.getId())).withRel("return"));
            }
        }

        return bookModel;
    }
}
