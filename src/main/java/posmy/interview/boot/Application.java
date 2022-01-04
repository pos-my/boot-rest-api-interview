package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import posmy.interview.boot.database.BookDao;
import posmy.interview.boot.database.TransactionDao;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.BookServiceImpl;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BookService bookService(BookDao bookDao, TransactionDao transactionDao){
        return new BookServiceImpl(bookDao, transactionDao);
    }
}
