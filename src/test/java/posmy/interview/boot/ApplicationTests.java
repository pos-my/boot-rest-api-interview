package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;

import java.io.File;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class ApplicationTests {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Users must be authorized in order to perform add user")
    void addUserRequiresAuthorization() throws Exception {
        mvc.perform(post("/user/add"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from adding member")
    void addUserForbiddenForMember() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(post("/user/add").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform update user")
    void updateUserRequiresAuthorization() throws Exception {
        mvc.perform(put("/user/update"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from updating member")
    void updateUserForbiddenForMember() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/existingUser.json"), User.class);
        mvc.perform(put("/user/update").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform delete user")
    void deleteUserRequiresAuthorization() throws Exception {
        mvc.perform(put("/user/delete"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from deleting member")
    void deleteUserForbiddenForMember() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/existingUser.json"), User.class);
        mvc.perform(put("/user/delete").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform view all user")
    void viewAllUserRequiresAuthorization() throws Exception {
        mvc.perform(get("/user/view/all?page=0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from viewing all member")
    void viewAllUserForbiddenForMember() throws Exception {
        mvc.perform(get("/user/view/all?page=0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform view user")
    void viewUserRequiresAuthorization() throws Exception {
        mvc.perform(get("/user/view?username=test2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from viewing specific member")
    void viewUserForbiddenForMember() throws Exception {
        mvc.perform(get("/user/view?username=test2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform delete own account")
    void deleteOwnAccRequiresAuthorization() throws Exception {
        mvc.perform(put("/user/delete/own"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian is forbidden from deleting own account")
    void deleteOwnAccForbiddenForLibrarian() throws Exception {
        mvc.perform(put("/user/delete/own"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform add book")
    void addBookRequiresAuthorization() throws Exception {
        mvc.perform(post("/book/add"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from adding book")
    void addBookForbiddenForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(post("/book/add").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform update book")
    void updateBookRequiresAuthorization() throws Exception {
        mvc.perform(put("/book/update"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from updating book")
    void updateBookForbiddenForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/update").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform delete book")
    void deleteBookRequiresAuthorization() throws Exception {
        mvc.perform(put("/book/delete"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member is forbidden from deleting book")
    void deleteBookForbiddenForMember() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/delete").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform view all book")
    void viewAllBookRequiresAuthorization() throws Exception {
        mvc.perform(get("/book/view/all?page=0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform borrow book")
    void borrowBookRequiresAuthorization() throws Exception {
        mvc.perform(put("/book/borrow"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Member is forbidden from borrowing book")
    void borrowBookForbiddenForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/existingBook.json"), Book.class);
        mvc.perform(put("/book/borrow").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Users must be authorized in order to perform return book")
    void returnBookRequiresAuthorization() throws Exception {
        mvc.perform(put("/book/return"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Member is forbidden from returning book")
    void returnBookForbiddenForLibrarian() throws Exception {
        Book book = objectMapper.readValue(new File("src/main/resources/json/integration/addBook.json"), Book.class);
        mvc.perform(put("/book/return").content(objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
