package posmy.interview.boot;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.UserDao;
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
class UserControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @Order(1)
    @DisplayName("Able to create new users with librarian role and member role")
    void createUsers() throws Exception {
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

    }

    @Test
    @Order(2)
    @DisplayName("Only librarian able to retrieve a list of users")
    void ensureOnlyLibrarianCanAccessGetUsersApi() throws Exception {
        //librarian
        mvc.perform(get("/users/")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1")
                .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.records", hasSize(2)));

        //member
        mvc.perform(get("/users/")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1")
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("Only librarian can perform profile update on any user")
    void ensureLibrarianCanPerformUpdateOnAnyUser() throws Exception {
        //librarian
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(1);
        updateUserRequest.setFullName("Captain America");
        mvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateUserRequest))
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Captain America"));

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(2);
        updateUserRequest.setFullName("James Bond");
        mvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateUserRequest))
                        .with(httpBasic("admin","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("James Bond"));
    }


    @Test
    @Order(4)
    @DisplayName("Member can perform profile update on their own user")
    void ensureMemberCanPerformUpdateOnOwnUser() throws Exception {
        //member, perform changes on other user
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(1);
        updateUserRequest.setFullName("Random name");
        mvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateUserRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is4xxClientError());

        //member, perform changes on own user
        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(2);
        updateUserRequest.setFullName("Spiderman");
        mvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateUserRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Spiderman"));

        //member delete own account
        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(2);
        updateUserRequest.setStatus(Constants.UserStatus.REMOVED.getType());
        mvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(updateUserRequest))
                        .with(httpBasic("jackson","password123")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constants.UserStatus.REMOVED.getType()));;
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
