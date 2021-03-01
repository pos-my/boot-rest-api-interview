package posmy.interview.boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class MemberControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("Users with role MEMBER are authorized to perform MEMBER actions")
    void memberCallMember() throws Exception {
        mvc.perform(get("/member/get"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DisplayName("Users with role LIBRARIAN are not authorized to perform MEMBER actions")
    void librarianCallMember() throws Exception {
        mvc.perform(get("/member/get"))
                .andExpect(status().isForbidden());
    }
}
