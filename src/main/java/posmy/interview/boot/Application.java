package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.database.BookDao;
import posmy.interview.boot.database.TransactionDao;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.BookServiceImpl;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.service.UserServiceImpl;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BookService bookService(BookDao bookDao, TransactionDao transactionDao, UserDao userDao){
        return new BookServiceImpl(bookDao, transactionDao, userDao);
    }

    @Bean
    public UserService userService(UserDao userDao, PasswordEncoder passwordEncoder){
        return new UserServiceImpl(userDao, passwordEncoder);
    }
}
