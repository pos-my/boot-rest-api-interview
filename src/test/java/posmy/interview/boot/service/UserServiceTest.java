package posmy.interview.boot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import posmy.interview.boot.configuration.PasswordConfiguration;
import posmy.interview.boot.model.enums.ErrorCodeEnum;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.result.UserQueryResult;
import posmy.interview.boot.model.result.UserServiceResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordConfiguration passwordConfiguration;

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createUserTest_success() {
        // Arrange
        UserRequest request =  buildUserRequest();

        // Action
        UserServiceResult actualResult = userService.createUser(request);

        // Assert
        assertThat(actualResult.isSuccess(), is(true));
        assertThat(actualResult.getUsername(), equalTo(request.getUsername()));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createUserTest_Idempotent() {
        // Arrange
        UserRequest request =  buildUserRequest();
        UserRequest idempotentUser =  buildUserRequest();

        // Action
        userService.createUser(request);
        UserServiceResult actualResult = userService.createUser(idempotentUser);

        // Assert
        assertThat(actualResult.isSuccess(), is(false));
        assertThat(actualResult.getUsername(), equalTo(request.getUsername()));
        assertThat(actualResult.getErrorContext(), equalTo(ErrorCodeEnum.IDEMPOTENT_USER.getCode()));
        assertThat(actualResult.getErrorDesc(), equalTo(ErrorCodeEnum.IDEMPOTENT_USER.getDescription()));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateUserTest_success() {
        // Arrange
        UserRequest request =  buildUserRequest();
        userService.createUser(request);

        // Action
        request.setMobileNo("updatedMobileNo");
        UserServiceResult actualResult = userService.updateUser(request);

        // Assert
        assertThat(actualResult.isSuccess(), is(true));
        assertThat(actualResult.getUsername(), equalTo(request.getUsername()));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateUserTest_userNotFound() {
        // Arrange
        UserRequest request =  buildUserRequest();
        userService.createUser(request);

        // Action
        request.setUsername("NoExistUserName");
        UserServiceResult actualResult = userService.updateUser(request);

        // Assert
        assertThat(actualResult.isSuccess(), is(false));
        assertThat(actualResult.getErrorContext(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getCode()));
        assertThat(actualResult.getErrorDesc(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getDescription()));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getUserByUserNameTest_success() {
        // Arrange
        UserRequest request =  buildUserRequest();
        userService.createUser(request);

        // Action
        UserQueryResult actualResult = userService.getUserByUserName(request.getUsername());

        // Assert
        assertThat(actualResult.isSuccess(), is(true));
        assertThat(actualResult.getUserList().get(0).getUsername(), equalTo(request.getUsername()));
        assertThat(actualResult.getUserList().get(0).getName(), equalTo(request.getName()));
        assertThat(actualResult.getUserList().get(0).getMobileNo(), equalTo(request.getMobileNo()));
    }

    @Test
    void getUserByUserNameTest_userNotFound() {

        // Action
        UserQueryResult actualResult = userService.getUserByUserName("UserNotExist");

        // Assert
        assertThat(actualResult.isSuccess(), is(false));
        assertThat(actualResult.getErrorContext(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getCode()));
        assertThat(actualResult.getErrorDesc(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getDescription()));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllUsers() {
        //Arrange
        UserRequest firstUserRequest = buildUserRequest();
        UserRequest secondUserRequest = buildUserRequest();
        secondUserRequest.setUsername("secondUser");

        userService.createUser(firstUserRequest);
        userService.createUser(secondUserRequest);

        // Action
        UserQueryResult actualResult = userService.getAllUsers();

        // Assert
        assertThat(actualResult.isSuccess(), is(true));
        assertThat(actualResult.getUserList().size(), equalTo(4));
    }

    @Test
    @Sql(scripts="classpath:data.sql",executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteUserByUserName() {
        // Arrange
        UserRequest request =  buildUserRequest();
        userService.createUser(request);

        // firstAssert
        assertThat(userService.getAllUsers().getUserList().size(), equalTo(3));

        // Action
        userService.deleteUserByUserName(request.getUsername());

        // second Assert
        UserQueryResult actualResult = userService.getUserByUserName(request.getUsername());
        assertThat(actualResult.isSuccess(), is(false));
        assertThat(actualResult.getErrorContext(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getCode()));
        assertThat(actualResult.getErrorDesc(), equalTo(ErrorCodeEnum.USER_NOT_FOUND.getDescription()));
    }

    private UserRequest buildUserRequest(){
        UserRequest request =  new UserRequest();
        request.setUsername("unitTestUserName");
        request.setName("unitTestName");
        request.setPassword(passwordConfiguration.passwordEncoder().encode("unitTestPassword"));
        request.setMobileNo("0000000000");

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_LIBRARIAN");
        request.setRoles(roles);
        return request;
    }
}