package posmy.interview.boot;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.model.book.CreateBookRequest;
import posmy.interview.boot.model.book.UpdateBookRequest;
import posmy.interview.boot.model.user.RegistrationRequest;
import posmy.interview.boot.model.user.UpdateUserRequest;
import posmy.interview.boot.util.Json;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() throws Exception {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @Order(1)
    @DisplayName("All users able to get list of books")
    void ensureAllUsersAbleToGetListOfBooks() throws Exception {
        RegistrationRequest registrationRequest = createLibrarianRole();
        mvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(registrationRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.UserStatus.ACTIVATED.toString()));

        registrationRequest = createMemberRole();
        mvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(registrationRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.UserStatus.ACTIVATED.toString()));

        //librarian
        mvc.perform(get("/books/")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1")
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.records").isArray());


        //member
        mvc.perform(get("/books/")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1")
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.records").isArray());
    }

    @Test
    @Order(2)
    @DisplayName("Only librarian able to add book")
    void ensureOnlyLibrarianCanCreateBook() throws Exception {
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setName("Avatar");
        createBookRequest.setDescription("Fiction");
        createBookRequest.setStatus(Constants.BookStatus.AVAILABLE.getType());

        //librarian
        mvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(createBookRequest))
                .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.BookStatus.AVAILABLE.toString()));

        //member
        createBookRequest = new CreateBookRequest();
        createBookRequest.setName("Final Fantasy");
        createBookRequest.setDescription("Fiction");
        createBookRequest.setStatus(Constants.BookStatus.AVAILABLE.getType());
        mvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(createBookRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    @DisplayName("Librarian able to update/remove book")
    void ensureLibrarianCanUpdateOrRemoveBook() throws Exception {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setName("Lean Startup");
        updateBookRequest.setDescription("Self help");
        updateBookRequest.setStatus(Constants.BookStatus.AVAILABLE.getType());

        //librarian update book name and description
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lean Startup"));


        //librarian update book status to deleted
        updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setStatus(Constants.BookStatus.BORROWED.getType());
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.BookStatus.BORROWED.getType()));

        //librarian update book status to borrow
        updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setStatus(Constants.BookStatus.AVAILABLE.getType());
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.BookStatus.AVAILABLE.getType()));

    }

    @Test
    @Order(3)
    @DisplayName("Member only able to borrow/return book")
    void ensureMemberOnlyCanBorrowOrReturnBook() throws Exception {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setStatus(Constants.BookStatus.BORROWED.getType());

        //member update book name and description
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.BookStatus.BORROWED.getType()));


        //member update book status to available/return
        updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setStatus(Constants.BookStatus.AVAILABLE.getType());
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.BookStatus.AVAILABLE.getType()));

        //member attempt to delete the book
        updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setId(1);
        updateBookRequest.setStatus(Constants.BookStatus.DELETED.getType());
        mvc.perform(put("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateBookRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is4xxClientError());
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

}
