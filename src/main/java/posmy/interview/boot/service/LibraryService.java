package posmy.interview.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.request.BookRequest;
import posmy.interview.boot.request.RoleRequest;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private static final Logger log = LoggerFactory.getLogger(LibraryService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<Book> getBooks() {
        List<Book> books = bookRepository.findAll();

        return books;
    }

    public BookRequest processBook(String action, String name, String currentRole) {
        Book book;
        BookRequest bookRequest = new BookRequest();
        if(action.equalsIgnoreCase("add")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Add book...");

                Optional<Book> existingBook = Optional.ofNullable(bookRepository.findByName(name));
                if (existingBook.isPresent()) {
                    existingBook.get().setBookStatus("AVAILABLE");
                    bookRepository.save(existingBook.get());
                    bookRequest = mapBookResponse(existingBook.get(), "SUCCESSFULLY ADDED BOOK");
                } else {

                    Book newBook = new Book();
                    newBook.setName(name);
                    newBook.setBookStatus("AVAILABLE");

                    bookRepository.save(newBook);
                    bookRequest = mapBookResponse(newBook, "SUCCESSFULLY ADDED BOOK");
                }
            }
            else {
                bookRequest.setStatus("Failed");
            }
        }
        else if(action.equalsIgnoreCase("borrow")){
            if (currentRole.equalsIgnoreCase("member")) {
                log.info("Borrow book...");

                book = bookRepository.findByName(name);
                if (book.getBookStatus().equalsIgnoreCase("AVAILABLE")) {
                    book.setBookStatus("BORROWED");
                    bookRepository.save(book);
                    bookRequest = mapBookResponse(book, "SUCCESSFULLY BORROWED BOOK");
                }
            }
            else {
                bookRequest.setStatus("Failed");
            }
        }
        else if(action.equalsIgnoreCase("return")){
            if (currentRole.equalsIgnoreCase("member")) {
                log.info("Return book...");

                book = bookRepository.findByName(name);
                if (book.getBookStatus().equalsIgnoreCase("BORROWED")) {
                    book.setBookStatus("AVAILABLE");
                    bookRepository.save(book);
                    bookRequest = mapBookResponse(book, "SUCCESSFULLY RETURNED BOOK");
                }
            }
            else {
                bookRequest.setStatus("Failed");
            }
        }
        else if(action.equalsIgnoreCase("update")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Update book...");

                book = bookRepository.findByName(name);
                book.setName(name);
                bookRepository.save(book);
                bookRequest.setName(book.getName());
                bookRequest = mapBookResponse(book, "SUCCESSFULLY UPDATED BOOK");
            }
            else {
                bookRequest.setStatus("Failed");
            }
        }
        else if(action.equalsIgnoreCase("remove")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Remove book...");

                book = bookRepository.findByName(name);
                if (book.getBookStatus().equalsIgnoreCase("AVAILABLE") ||
                        book.getBookStatus().equalsIgnoreCase("BORROWED")) {
                    book.setBookStatus("REMOVED");
                    bookRepository.save(book);
                    bookRequest = mapBookResponse(book, "SUCCESSFULLY REMOVED BOOK");
                }
            }
            else {
                bookRequest.setStatus("Failed");
            }
        }
        else if(action.equalsIgnoreCase("view")){
            if (currentRole.equalsIgnoreCase("member")) {
                log.info("View book...");
                book = bookRepository.findByName(name);
                bookRequest = mapBookResponse(book, "SUCCESSFULLY RETRIEVED BOOK");
            }
        }

        return bookRequest;
    }

    public RoleRequest processRole(String action, String name, String currentRole) {
        Role role;
        RoleRequest roleRequest = new RoleRequest();
        if(action.equalsIgnoreCase("add")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Adding new member...");

                Optional<Role> existingRole = Optional.ofNullable(roleRepository.findByName(name));
                if (existingRole.isPresent()) {

                    existingRole.get().setRoleStatus("ACTIVE");
                    roleRepository.save(existingRole.get());

                    roleRequest = mapRoleResponse(existingRole.get(), "SUCCESSFULLY ADDED MEMBER");
                } else {
                    Role newRole = new Role();

                    newRole.setName(name);
                    newRole.setRoleStatus("ACTIVE");
                    newRole.setRole("member");

                    roleRepository.save(newRole);
                    roleRequest = mapRoleResponse(newRole, "SUCCESSFULLY ADDED MEMBER");
                }
            }
        }
        else if(action.equalsIgnoreCase("update")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Update member...");

                role = roleRepository.findByName(name);
                role.setName(name);
                roleRepository.save(role);
                roleRequest = mapRoleResponse(role, "SUCCESSFULLY UPDATED MEMBER");
            }

        }
        else if(action.equalsIgnoreCase("remove")){
            if (currentRole.equalsIgnoreCase("librarian") ||
                    currentRole.equalsIgnoreCase("member")) {
                log.info("Removing member...");

                role = roleRepository.findByName(name);
                role.setRoleStatus("REMOVED");
                roleRepository.save(role);
                roleRequest = mapRoleResponse(role, "SUCCESSFULLY REMOVED MEMBER");
            }
        }
        else if(action.equalsIgnoreCase("view")){
            if (currentRole.equalsIgnoreCase("librarian")) {
                log.info("Viewing member...");

                role = roleRepository.findByName(name);
                roleRequest = mapRoleResponse(role, "SUCCESSFULLY RETRIEVED MEMBER");
            }
        }

        return roleRequest;
    }

    public BookRequest mapBookResponse(Book book, String status){
        BookRequest bookRequest = new BookRequest();

        bookRequest.setName(book.getName());
        bookRequest.setBookStatus(book.getBookStatus());
        bookRequest.setStatus(status);

        return bookRequest;
    }

    public RoleRequest mapRoleResponse(Role role, String status){
        RoleRequest roleRequest = new RoleRequest();

        roleRequest.setName(role.getName());
        roleRequest.setRole(role.getRole());
        roleRequest.setRoleStatus(role.getRoleStatus());
        roleRequest.setStatus(status);

        return roleRequest;
    }

}
