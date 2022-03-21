package posmy.interview.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.AppUserService;
import posmy.interview.boot.service.BookService;

import java.util.ArrayList;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserService userService,
                          BookService bookService){
        return args -> {
            userService.saveUserRole(new AppUserRole(null, "LIBRARIAN"));
            userService.saveUserRole(new AppUserRole(null, "MEMBER"));

            userService.saveUser(new AppUser(null, "Sean", "sean", "1234", 27, new ArrayList<>()));
            userService.saveUser(new AppUser(null, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>()));
            userService.saveUser(new AppUser(null, "Terry", "terry", "1234", 32, new ArrayList<>()));
            userService.saveUser(new AppUser(null, "Jensen", "jensen", "1234", 28, new ArrayList<>()));
            userService.saveUser(new AppUser(null, "Ooi JW", "jw", "1234", 28, new ArrayList<>()));

            userService.addRoleToUser("sean", "LIBRARIAN");
            userService.addRoleToUser("zz", "MEMBER");
            userService.addRoleToUser("terry", "MEMBER");
            userService.addRoleToUser("jensen", "MEMBER");
            userService.addRoleToUser("jw", "MEMBER");

            bookService.saveBook(new Book(null, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013));
            bookService.saveBook(new Book(null, "0-9009-8552-6", "Men are from Mars Women are from Venus", "Emily", "AVAILABLE", null, "English", 2018));
            bookService.saveBook(new Book(null, "0-2243-3869-2", "Brave New World", "Lois", "AVAILABLE", null, "English", 2010));
            bookService.saveBook(new Book(null, "0-7686-2345-1", "About Time", "Margot Robbie", "BORROWED", userService.getUser("zz"), "English", 2010));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
