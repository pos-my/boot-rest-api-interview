package posmy.interview.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.constant.Constant;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, BookService bookService) {
        return args -> {
            Date date = new Date();
            User librarian = new User("", "librarian", "12345", Constant.UserRole.LIBRARIAN.name(), Constant.UserStatus.ACTIVE.name(), date, date);
            List<User> members = IntStream.rangeClosed(1, 2).mapToObj(obj -> new User("", "test" + obj, "12345", Constant.UserRole.MEMBER.name(), Constant.UserStatus.ACTIVE.name(), date, date)).collect(Collectors.toList());
            List<Book> book = IntStream.rangeClosed(1, 2).mapToObj(obj -> new Book("", "title" + obj, "author1", "0000" + obj, Constant.BookStatus.AVAILABLE.name(), null, date, date)).collect(Collectors.toList());
            members.add(librarian);
            members.stream().forEach(obj -> userService.addUser(obj));
            book.stream().forEach(obj -> bookService.addBook(obj));
        };
    }
}
