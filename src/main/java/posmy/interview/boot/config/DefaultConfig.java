package posmy.interview.boot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.api.BookService;
import posmy.interview.boot.service.api.RoleService;
import posmy.interview.boot.service.api.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static posmy.interview.boot.constant.Constant.LIBRARIAN;
import static posmy.interview.boot.constant.Constant.MEMBER;

@Configuration
public class DefaultConfig {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;


    /**
     * Insert initial data for testing purpose
     */
    @PostConstruct
    void insertTestData() {
        //insert all the roles
        var roles = roleService.saveAll(
                List.of(
                        Role.builder()
                                .name("LIBRARIAN")
                                .build()
                        ,
                        Role.builder()
                                .name("MEMBER")
                                .build()
                )
        );
        var memberRole = roles.stream().filter(r -> r.getName().equals(MEMBER)).collect(Collectors.toList());
        var librarianRole = roles.stream().filter(r -> r.getName().equals(LIBRARIAN)).collect(Collectors.toList());
        // insert users with roles
        userService.saveAll(
                List.of(
                        User.builder()
                                .username("ck")
                                .password("pa@@12.X9")
                                .roles(roles)
                                .build(),
                        User.builder()
                                .username("staff")
                                .password("qt##12.Y5")
                                .roles(librarianRole)
                                .build()
                        ,
                        User.builder()
                                .username("member01")
                                .password("tx##12.B3")
                                .roles(memberRole)
                                .build()
                        ,
                        User.builder()
                                .username("member02")
                                .password("q5##12.N8")
                                .roles(memberRole)
                                .build()
                )
        );

        //insert some books
        bookService.saveAll(
                List.of(
                        Book.builder()
                                .name("Wolf Hall")
                                .author("Hilary Mantel")
                                .publishYear(2009)
                                .build(),
                        Book.builder()
                                .name("Gilead")
                                .author("Marilynne Robinson")
                                .publishYear(2004)
                                .build()
                        ,
                        Book.builder()
                                .name("Secondhand Time")
                                .author("Svetlana Alexievich")
                                .publishYear(2013)
                                .build(),
                        Book.builder()
                                .name("Secondhand Time")
                                .author("translated by Bela Shayevich")
                                .publishYear(2016)
                                .build()
                )
        );

    }

    /**
     * Create Object mapper to let it can ser any class that come with field
     *
     * @return Object mapper
     */
    @Bean
    ObjectMapper mapper() {
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

}
