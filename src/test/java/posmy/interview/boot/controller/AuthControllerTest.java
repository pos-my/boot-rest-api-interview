package posmy.interview.boot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.controller.request.TokenRefreshRequest;
import posmy.interview.boot.controller.request.UserAuthRequest;
import posmy.interview.boot.db.RefreshTokenRepository;
import posmy.interview.boot.db.UserRepository;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.RefreshToken;
import posmy.interview.boot.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Member invalid login")
    void memberLoginInvalid() throws Exception {

        UserAuthRequest request = new UserAuthRequest();
        request.setUsername("testing");
        request.setPassword("password");
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/member/login").content(requestBody).header("Content-Type", "application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Librarian invalid login")
    void librarianLoginInvalid() throws Exception {

        UserAuthRequest request = new UserAuthRequest();
        request.setUsername("testing");
        request.setPassword("password");
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/librarian/login").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Member login")
    void memberLogin() throws Exception {

        String username = "testing";
        String password = "password";

        User mockUser = User.builder().username(username)
                .id(1L)
                .password(password)
                .role(UserRole.MEMBER)
                .state(UserState.ACTIVE)
                .createdTime(Instant.now())
                .updatedTime(Instant.now())
                .build();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setUserId(1L);
        refreshToken.setToken("token");
        refreshToken.setCreatedTime(Instant.now());
        refreshToken.setExpiryTime(Instant.now().plus(10, ChronoUnit.HOURS));

        given(userRepository.findByUsernameAndRoleAndState(username, UserRole.MEMBER, UserState.ACTIVE)).willReturn(mockUser);
        given(userRepository.findByUsername(username)).willReturn(mockUser);
        given(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).willReturn(true);
        given(refreshTokenRepository.save(Mockito.any())).willReturn(refreshToken);

        UserAuthRequest request = new UserAuthRequest();
        request.setUsername(username);
        request.setPassword(password);
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/member/login").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Librarian login")
    void librarianLogin() throws Exception {

        String username = "testing";
        String password = "password";

        User mockUser = User.builder().username(username)
                .id(1L)
                .password(password)
                .role(UserRole.LIBRARIAN)
                .state(UserState.ACTIVE)
                .createdTime(Instant.now())
                .updatedTime(Instant.now())
                .build();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setUserId(1L);
        refreshToken.setToken("token");
        refreshToken.setCreatedTime(Instant.now());
        refreshToken.setExpiryTime(Instant.now().plus(10, ChronoUnit.HOURS));

        given(userRepository.findByUsernameAndRoleAndState(username, UserRole.LIBRARIAN, UserState.ACTIVE)).willReturn(mockUser);
        given(userRepository.findByUsername(username)).willReturn(mockUser);
        given(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).willReturn(true);
        given(refreshTokenRepository.save(Mockito.any())).willReturn(refreshToken);

        UserAuthRequest request = new UserAuthRequest();
        request.setUsername(username);
        request.setPassword(password);
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/librarian/login").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Refresh token")
    void refreshToken() throws Exception {
        String username = "testing";
        String password = "password";
        String token = "refreshToken";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(1L);
        refreshToken.setToken(token);
        refreshToken.setExpiryTime(Instant.now().plus(10, ChronoUnit.HOURS));
        refreshToken.setCreatedTime(Instant.now());
        User mockUser = User.builder().username(username)
                .id(1L)
                .password(passwordEncoder.encode(password))
                .role(UserRole.LIBRARIAN)
                .state(UserState.ACTIVE)
                .createdTime(Instant.now())
                .updatedTime(Instant.now())
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));
        given(refreshTokenRepository.findByToken(refreshToken.getToken())).willReturn(refreshToken);

        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken(token);
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/refreshToken").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Refresh token expired")
    void refreshTokenExpired() throws Exception {
        String username = "testing";
        String password = "password";
        String token = "refreshToken";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(1L);
        refreshToken.setToken(token);
        refreshToken.setExpiryTime(Instant.now().minus(10, ChronoUnit.HOURS));
        refreshToken.setCreatedTime(Instant.now());
        User mockUser = User.builder().username(username)
                .id(1L)
                .password(passwordEncoder.encode(password))
                .role(UserRole.LIBRARIAN)
                .state(UserState.ACTIVE)
                .createdTime(Instant.now())
                .updatedTime(Instant.now())
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));
        given(refreshTokenRepository.findByToken(refreshToken.getToken())).willReturn(refreshToken);

        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken(token);
        String requestBody = mapper.writeValueAsString(request);

        mvc.perform(post("/auth/refreshToken").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
    }
}
