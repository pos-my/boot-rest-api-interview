package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.constant.Constant;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.impl.UserImpl;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserImpl userImpl;
    @Autowired
    BookService bookService;

    private MockMvc mvc;

    @Test
    @Order(1)
    @DisplayName("Update non existing user")
    void updateNonExistingUser() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/addUser.json"), User.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(existingUser);
        });

        assertEquals(Constant.USER_ERROR_USER_NOT_EXIST, entityNotFoundException.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("Delete non existing user")
    void deleteNonExistingUser() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/addUser.json"), User.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(existingUser);
        });

        assertEquals(entityNotFoundException.getMessage(), Constant.USER_ERROR_USER_NOT_EXIST);
    }

    @Test
    @Order(3)
    @DisplayName("Delete librarian")
    void deleteLibrarian() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/existingLibrarian.json"), User.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(existingUser);
        });
        assertEquals(Constant.USER_ERROR_LIB_NOT_ALL_DEL, runtimeException.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Delete member that has book")
    void deleteMemberWithBook() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/existingUser.json"), User.class);
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook2.json"), Book.class);
        bookService.borrowBook(existingBook, existingUser.getUsername());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(existingUser);
        });
        bookService.returnBook(existingBook, existingUser.getUsername());
        assertEquals(runtimeException.getMessage(), Constant.USER_ERROR_HAS_BOOK);
    }

    @Test
    @Order(5)
    @DisplayName("Delete member that has book")
    void deleteMember() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/existingUser.json"), User.class);
        User deletedUser = userService.deleteUser(existingUser);
        assertThat(deletedUser.getStatus().equals(Constant.UserStatus.INACTIVE));
    }

    @Test
    @Order(6)
    @DisplayName("Delete inactive member")
    void deleteInactiveMember() throws Exception {
        User existingUser = objectMapper.readValue(new File("src/main/resources/json/service/existingUser.json"), User.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(existingUser);
        });
        assertEquals(runtimeException.getMessage(), Constant.USER_ERROR_USER_IS_INACTIVE);
    }

    @Test
    @Order(7)
    @DisplayName("View all member")
    void viewAllMembers() throws Exception {
        Page<User> users = userService.getMembers(0);
        assertThat(users.getContent().stream().anyMatch(user -> user.getUsername().equals("member3")));
    }

    @Test
    @Order(8)
    @DisplayName("View non existing member")
    void viewAllNonExistingMember() throws Exception {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.getMemberByUsername("asdasd");
        });
        assertEquals(entityNotFoundException.getMessage(), Constant.USER_ERROR_USER_NOT_EXIST);
    }

    @Test
    @Order(9)
    @DisplayName("View existing member")
    void viewMemberByUsername() throws Exception {
        User user = userService.getMemberByUsername("test1");
        assertThat(user.getUsername().equals("test1"));
    }

    @Test
    @Order(10)
    @DisplayName("Delete own account that doesn't exist")
    void deleteOwnAccountThatDoesNotExist() throws Exception {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteOwnAccount("asdasd");
        });
        assertEquals(entityNotFoundException.getMessage(), Constant.USER_ERROR_USER_NOT_EXIST);
    }

    @Test
    @Order(10)
    @DisplayName("Delete own account")
    void deleteOwnAccount() throws Exception {
        User user = userService.deleteOwnAccount("test1");
        assertThat(user.getStatus().equals(Constant.UserStatus.INACTIVE));
    }

    @Test
    @Order(11)
    @DisplayName("Load user using spring security user details with inactive acc")
    void loadUserByUsernameInactive() throws Exception {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            UserDetails user = userImpl.loadUserByUsername("test1");
        });
        assertEquals(runtimeException.getMessage(), Constant.USER_ERROR_USER_IS_INACTIVE);
    }

    @Test
    @Order(12)
    @DisplayName("Load user using spring security user details with non existing acc")
    void loadUserByUsernameNotFound() throws Exception {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            UserDetails user = userImpl.loadUserByUsername("asdasd");
        });
        assertEquals(runtimeException.getMessage(), Constant.USER_ERROR_USER_NOT_EXIST);
    }

    @Test
    @Order(13)
    @DisplayName("Load user using spring security user details with existing acc")
    void loadUserByUsername() throws Exception {
        UserDetails user = userImpl.loadUserByUsername("test2");
        assertThat(user.getUsername().equals("test2"));
    }
}
