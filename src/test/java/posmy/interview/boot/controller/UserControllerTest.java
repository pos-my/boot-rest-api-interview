package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.controller.request.AddUserRequest;
import posmy.interview.boot.db.UserRepository;
import posmy.interview.boot.entity.UserIdAndRole;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.jwt.JwtUtils;
import posmy.interview.boot.security.service.UserDetailsImpl;
import posmy.interview.boot.security.service.UserDetailsServiceImpl;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RequiredArgsConstructor
public class UserControllerTest {

    private final WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void before() {
        mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void fetchAllMembers() throws Exception {
        mockLibrarianAccessToken();

        mockMvc.perform(get("/user/member/list").header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addMember() throws Exception {
        mockLibrarianAccessToken();
        String username = "Member A";
        String password = "Password A";

        AddUserRequest request = new AddUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setRole(UserRole.MEMBER);
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserRole.MEMBER);
        user.setState(UserState.ACTIVE);

        String requestBody = mapper.writeValueAsString(request);
        given(userRepository.save(Mockito.any())).willReturn(user);

        mockMvc.perform(post("/user/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateMember() throws Exception {
        mockLibrarianAccessToken();

        User user = new User();
        user.setId(1L);
        user.setUsername("TestB");
        user.setPassword("passwordB");
        user.setRole(UserRole.MEMBER);
        user.setState(UserState.ACTIVE);
        String requestBody = mapper.writeValueAsString(user);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(put("/user/member/update").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void librarianDeleteMember() throws Exception {
        mockLibrarianAccessToken();

        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(UserRole.MEMBER);
        user.setState(UserState.ACTIVE);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String memberIdToBeRemoved = "1";
        mockMvc.perform(delete("/user/member/delete/" + memberIdToBeRemoved)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void memberDeleteMember() throws Exception {
        mockMemberAccessToken();

        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(UserRole.MEMBER);
        user.setState(UserState.ACTIVE);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String memberIdToBeRemoved = "1";
        mockMvc.perform(delete("/user/member/delete/" + memberIdToBeRemoved)
                    .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void memberDeleteOtherMember() throws Exception {
        mockMemberAccessToken();

        String memberIdToBeRemoved = "2";
        Assertions.assertThatThrownBy(
                () -> mockMvc.perform(delete("/user/member/delete/" + memberIdToBeRemoved)
                        .header("Authorization", "Bearer accesTokenAbc123"))
        ).hasCauseInstanceOf(Exception.class).hasMessageContaining("Unable to delete other user");
    }

    private void mockMemberAccessToken() {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, UserRole.MEMBER);
        User user = User.builder()
                .id(1L).username("username").password("password")
                .role(UserRole.MEMBER).state(UserState.ACTIVE)
                .createdTime(Instant.now()).updatedTime(Instant.now())
                .build();
        UserDetails userDetails = UserDetailsImpl.buildUser(user);

        given(jwtUtils.validateJwtToken(Mockito.anyString())).willReturn(true);
        given(jwtUtils.getUserRoleFromJwtToken(Mockito.anyString())).willReturn(userIdAndRole);
        given(userDetailsService.loadUserById(userIdAndRole.getId())).willReturn(userDetails);
    }

    private void mockLibrarianAccessToken() {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, UserRole.LIBRARIAN);
        User user = User.builder()
                .id(1L).username("username").password("password")
                .role(UserRole.LIBRARIAN).state(UserState.ACTIVE)
                .createdTime(Instant.now()).updatedTime(Instant.now())
                .build();
        UserDetails userDetails = UserDetailsImpl.buildUser(user);

        given(jwtUtils.validateJwtToken(Mockito.anyString())).willReturn(true);
        given(jwtUtils.getUserRoleFromJwtToken(Mockito.anyString())).willReturn(userIdAndRole);
        given(userDetailsService.loadUserById(userIdAndRole.getId())).willReturn(userDetails);
    }
}
