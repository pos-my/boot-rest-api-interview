package posmy.interview.boot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.BookDao;
import posmy.interview.boot.database.TransactionDao;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.model.book.BookCreatedResponse;
import posmy.interview.boot.model.book.BookResponse;
import posmy.interview.boot.model.book.UpdateBookResponse;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.model.database.UserEntity;
import posmy.interview.boot.model.user.GetUserResponse;
import posmy.interview.boot.model.user.RegistrationRequest;
import posmy.interview.boot.model.user.UpdateUserRequest;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BookServiceUnitTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @Order(1)
    @DisplayName("Able to create new users")
    void createUsers() throws Exception {
        RegistrationRequest registrationRequest = createLibrarianRole();
        userService.processRegistrationRequest(registrationRequest.getUsername(), registrationRequest.getFullName(),
                registrationRequest.getPassword(), registrationRequest.getRole());
        UserEntity userEntity = userDao.findUserEntityByUserName(registrationRequest.getUsername());
        Assertions.assertThat(userEntity.getUserName()).isEqualTo(registrationRequest.getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("Able to create new book")
    void createNewBook() throws Exception {
        String name = "7 habits";
        String description = "Self Help";
        String status = Constants.BookStatus.AVAILABLE.getType();
        BookCreatedResponse bookCreatedResponse = bookService.createBook(name, description, status);
        BookEntity bookEntity = bookDao.findByBookId(bookCreatedResponse.getBookId());
        Assertions.assertThat(bookEntity.getName()).isEqualTo(name);
    }

    @Test
    @Order(3)
    @DisplayName("Able to update book")
    @WithUserDetails("admin") //mock the auth admin
    void updateBook() throws Exception {
        int id = 1;
        String name = "Power of now";
        UpdateBookResponse updateBookResponse = bookService.updateBook(id, name, null, null);
        BookEntity bookEntity = bookDao.findByBookId(updateBookResponse.getBookId());
        Assertions.assertThat(bookEntity.getName()).isEqualTo(name);
    }

    @Test
    @Order(4)
    @DisplayName("Able to get list of books")
    @WithUserDetails("admin") //mock the auth admin
    void getListOfBooks() throws Exception {
        BookResponse bookResponse = bookService
                .processBookQueryRequest(10, 1, null, null, null);
        Assertions.assertThat(bookResponse.getRecords()).hasSizeGreaterThan(0);
    }



    private RegistrationRequest createLibrarianRole(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("admin");
        registrationRequest.setFullName("Aaron");
        registrationRequest.setPassword("password123");
        registrationRequest.setRole(Constants.ROLE_LIBRARIAN);
        return registrationRequest;
    }

    private RegistrationRequest createMemberRole(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("jackson");
        registrationRequest.setFullName("Jackson Wang");
        registrationRequest.setPassword("password123");
        registrationRequest.setRole(Constants.ROLE_MEMBER);
        return registrationRequest;
    }

    private RegistrationRequest create2ndMemberRole(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("jack");
        registrationRequest.setFullName("Jack Ma");
        registrationRequest.setPassword("password123");
        registrationRequest.setRole(Constants.ROLE_MEMBER);
        return registrationRequest;
    }

}
