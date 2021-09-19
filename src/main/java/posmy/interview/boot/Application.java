package posmy.interview.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
 	@Bean
    public CommandLineRunner bookDemo(BookService bookService, UserService userService) {
        return (args) -> {
            // create default books & user
        	bookService.addBook(Book.builder().isbn("123789654").title("Book 1").build());
        	bookService.addBook(Book.builder().isbn("789654987").title("Book 2").build());
        	bookService.addBook(Book.builder().isbn("321456987").title("Book 3").build());
        	bookService.addBook(Book.builder().isbn("123456987").title("Book 4").build());
        	userService.addUser(User.builder().username("test").password("test").role(Role.LIBRARIAN).build());
        	userService.addUser(User.builder().username("test2").password("test").role(Role.MEMBER).build());
        };
    }
}
