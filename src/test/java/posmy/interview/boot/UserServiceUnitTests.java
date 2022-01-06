package posmy.interview.boot;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.model.database.UserEntity;
import posmy.interview.boot.model.user.GetUserResponse;
import posmy.interview.boot.model.user.RegistrationRequest;
import posmy.interview.boot.model.user.UpdateUserRequest;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.util.Json;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserServiceUnitTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

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
    @DisplayName("Able to get list of users")
    void getListOfUsers() throws Exception {
        GetUserResponse getUserResponse = userService.getUsers(20, 1, null, null, null);
        Assertions.assertThat(getUserResponse.getRecords()).hasSizeGreaterThan(0);
    }

    @Test
    @Order(3)
    @DisplayName("Able to get update user")
    @WithUserDetails("admin") //mock the auth admin
    void updateUser() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(1);
        updateUserRequest.setFullName("Aaron Liew");
        userService.updateUserRequest(updateUserRequest);
        UserEntity userEntity = userDao.findUserEntityByUserId(updateUserRequest.getUserId());
        Assertions.assertThat(userEntity.getFullName()).isEqualTo(updateUserRequest.getFullName());
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
