package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.model.User;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void viewMembers() throws Exception {
        mvc.perform(get("/viewMembers"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addMember() throws Exception {
        User user = new User();
        user.setUserId(2);
        user.setUserName("Alan");
        user.setUserPassword("alanpassword");
        user.setUserRole("LIBRARIAN");
        String userString = mapper.writeValueAsString(user);
        System.out.println(userString);
        mvc.perform(post("/addMember").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateMember() throws Exception {
        User user = new User();
        user.setUserId(2);
        user.setUserName("James");
        user.setUserPassword("jamespassword");
        user.setUserRole("MEMBER");
        String userString = mapper.writeValueAsString(user);
        System.out.println(userString);
        mvc.perform(put("/updateMember").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeMember() throws Exception {
        String memberIdToBeRemoved = "2";
        mvc.perform(delete("/removeMember/" + memberIdToBeRemoved))
                .andExpect(status().is2xxSuccessful());
    }
}