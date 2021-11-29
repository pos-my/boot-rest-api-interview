package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.entity.User;

import java.io.File;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

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
    @Order(1)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to add new member")
    void addUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(post("/user/add").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to add existing member")
    void addExistingUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(post("/user/add").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to update existing member")
    void updateExistingUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(put("/user/update").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to delete existing member")
    void deleteExistingUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(put("/user/delete").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to update non existing member")
    void updateNewUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(put("/user/update").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(6)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to delete non existing member")
    void deleteNewUserForLibrarian() throws Exception {
        User user = objectMapper.readValue(new File("src/main/resources/json/integration/addUser.json"), User.class);
        mvc.perform(put("/user/delete").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(7)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to view all member")
    void viewAllUserForLibrarian() throws Exception {
        mvc.perform(get("/user/view/all?page=0").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to view all member with wrong param")
    void viewAllUserWithWrongParamForLibrarian() throws Exception {
        mvc.perform(get("/user/view/all?page=-14").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(9)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to view member with username")
    void viewUserWithUsernameForLibrarian() throws Exception {
        mvc.perform(get("/user/view?username=test1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to view member with wrong param")
    void viewUserWithWrongParamForLibrarian() throws Exception {
        mvc.perform(get("/user/view?username=$%$%^&%").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(10)
    @WithMockUser(authorities = "LIBRARIAN")
    @DisplayName("Librarian to delete own account")
    void deleteOwnAccountForLibrarian() throws Exception {
        mvc.perform(put("/user/delete/own").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    @WithMockUser(authorities = "MEMBER")
    @DisplayName("Member to delete own account without login")
    void deleteOwnAccountWithoutLoginAuthentication() throws Exception {
        mvc.perform(put("/user/delete/own").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(12)
    @WithMockUser(username = "test1", authorities = "MEMBER")
    @DisplayName("Member to delete own account without login")
    void deleteOwnAccountForMember() throws Exception {
        mvc.perform(put("/user/delete/own").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
