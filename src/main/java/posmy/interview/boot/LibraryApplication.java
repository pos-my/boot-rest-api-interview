package posmy.interview.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.RoleRepository;

@SpringBootApplication
@ComponentScan("posmy.interview.boot")
public class LibraryApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("StartApplication...");

        //Inserting book data to temp DB
        Book book1 = new Book();
        Book book2 = new Book();

        book1.setBookStatus("AVAILABLE");
        book1.setName("JavaGuide");

        book2.setBookStatus("AVAILABLE");
        book2.setName("CovidCure");

        bookRepository.save(book1);
        bookRepository.save(book2);

        //Inserting librarian and member data to temp DB
        Role role1 = new Role();
        Role role2 = new Role();
        Role role3 = new Role();

        role1.setName("Jane");
        role1.setRole("librarian");
        role1.setRoleStatus("ACTIVE");

        role2.setName("Tom");
        role2.setRole("member");
        role2.setRoleStatus("ACTIVE");

        role3.setName("Peter");
        role3.setRole("member");
        role3.setRoleStatus("ACTIVE");

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);
    }

}
