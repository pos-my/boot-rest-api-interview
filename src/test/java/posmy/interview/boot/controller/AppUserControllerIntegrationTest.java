package posmy.interview.boot.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.util.TokenUtil;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerIntegrationTest {
    @Value("${security.authentication.secret}")
    private String secret;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Gson gson;

    @Test
    void canGetUser() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );

        //when
        //then
        AppUser expectedUser = new AppUser(null, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        mvc.perform(get("/user/get?username=" + expectedUser.getUsername())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(expectedUser.getUsername())))
                .andExpect(jsonPath("$.name", is(expectedUser.getName())));
    }

    @Test
    void canGetUsers() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );

        //when
        //then
        mvc.perform(get("/user/get-all-users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*").isNotEmpty());
    }

    @Test
    void canSaveUser() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        AppUser userToBeSave = new AppUser(null, "Jazz", "jazz", "1234", 33, new ArrayList<>());

        //when
        //then
        mvc.perform(post("/user/save")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(gson.toJson(userToBeSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username",is(userToBeSave.getUsername())))
                .andExpect(jsonPath("$.name", is(userToBeSave.getName())));
    }

    @Test
    void canSaveRole() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        AppUserRole roleToBeSave = new AppUserRole(null, "ADMIN");

        //when
        //then
        mvc.perform(post("/user/role/save")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(roleToBeSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",is(roleToBeSave.getName())));
    }

    @Test
    void canAddRoleToUser() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        String username = "zz";
        String role = "LIBRARIAN";

        //when
        //then
        mvc.perform(post("/user/role/add-to-user?username="+username+"&roleName="+role)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk());
    }

    @Test
    void canRemoveRoleFromUser() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        String username = "jw";
        String role = "MEMBER";

        //when
        //then
        mvc.perform(post("/user/role/remove-from-user?username="+username+"&roleName="+role)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk());
    }

    @Test
    void canRemoveUser() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        String username = "terry";

        //when
        //then
        mvc.perform(delete("/user/remove?username="+username)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk());
    }

    @Test
    void canSelfRemove() throws Exception {
        //given
        String member_access_token = TokenUtil.generateAccessToken(
                "jensen",
                "/login",
                Collections.singletonList("MEMBER"),
                secret
        );

        //when
        //then
        mvc.perform(delete("/user/self-remove")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_access_token))
                .andExpect(status().isOk());
    }

    @Test
    void canRefreshToken() throws Exception {
        //given
        String member_refresh_token = TokenUtil.generateRefreshToken(
                "zz",
                "/login",
                secret
        );

        //when
        //then
        mvc.perform(get("/user/token/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_refresh_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andExpect(jsonPath("$.refresh_token").isNotEmpty());
    }
}