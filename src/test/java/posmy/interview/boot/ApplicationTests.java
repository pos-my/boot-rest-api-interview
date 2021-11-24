package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Users must be authorized in order to perform actions")
    void contextLoads() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Librarian have access to librarian API")
    @WithMockUser(roles = "LIBRARIAN")
    void contextLoads_librarian_success() throws Exception {
        mvc.perform(get("/librarian/member?page=0&size=1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Librarian have no access to member API")
    @WithMockUser(roles = "LIBRARIAN")
    void contextLoads_librarian_fail() throws Exception {
        mvc.perform(get("/member/book?page=0&size=1"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Member have access to member API")
    @WithMockUser(roles = "MEMBER")
    void contextLoads_member_success() throws Exception {
        mvc.perform(get("/member/book?page=0&size=1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Member have no access to librarian API")
    @WithMockUser(roles = "MEMBER")
    void contextLoads_member_fail() throws Exception {
        mvc.perform(get("/librarian/member?page=0&size=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("HTTP authentication works for librarian user accessing librarian API")
    void contextLoads_librarian_http_success() throws Exception {
        mvc.perform(get("/librarian/member?page=0&size=1").with(httpBasic("librarian","p@ssw0rd")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("HTTP authentication does not work for librarian user accessing member API")
    void contextLoads_librarian_http_fail() throws Exception {
        mvc.perform(get("/member/book?page=0&size=1").with(httpBasic("librarian","p@ssw0rd")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("HTTP authentication works for member user accessing member API")
    void contextLoads_member_http_success() throws Exception {
        mvc.perform(get("/member/book?page=0&size=1").with(httpBasic("member","p@ssw0rd")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("HTTP authentication does not work for member user accessing librarian API")
    void contextLoads_member_http_fail() throws Exception {
        mvc.perform(get("/librarian/member?page=0&size=1").with(httpBasic("member","p@ssw0rd")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User which have no authentication could not access API")
    void contextLoads_unauthorized_http() throws Exception {
        mvc.perform(get("/librarian/member?page=0&size=1").with(httpBasic("randomstranger","randomstranger")))
                .andExpect(status().isUnauthorized());
    }
}
