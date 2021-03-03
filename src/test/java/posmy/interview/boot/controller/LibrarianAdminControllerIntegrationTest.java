package posmy.interview.boot.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.request.MemberPatchRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibrarianAdminControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private HttpHeaders headers;

    private final String existingUsername = "user999";
    private final String existingPassword = "pass";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user001:pass"));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @AfterEach
    void teardown() {
        if (inMemoryUserDetailsManager.userExists(existingUsername))
            inMemoryUserDetailsManager.deleteUser(existingUsername);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add MEMBER")
    public void whenAdminMemberAddThenSuccess() {
        MemberAddRequest request = MemberAddRequest.builder()
                .user("user100")
                .pass("pass100")
                .build();

        assertThat(inMemoryUserDetailsManager.userExists(request.getUser()))
                .isFalse();

        HttpEntity<MemberAddRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/add"),
                HttpMethod.POST,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(inMemoryUserDetailsManager.userExists(request.getUser()))
                .isTrue();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add Duplicate MEMBER")
    public void givenDuplicateUserWhenAdminMemberAddThenError() {
        UserDetails existingUser = setupExistingUser();
        MemberAddRequest request = MemberAddRequest.builder()
                .user(existingUser.getUsername())
                .pass(existingUser.getPassword())
                .build();

        assertThat(inMemoryUserDetailsManager.userExists(request.getUser()))
                .isTrue();

        HttpEntity<MemberAddRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/add"),
                HttpMethod.POST,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER username")
    public void whenAdminMemberPatchUsernameThenSuccess() {
        UserDetails existingUser = setupExistingUser();
        String oldUsername = existingUser.getUsername();
        String newUsername = oldUsername + "_NEW";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.USER.name().toLowerCase())
                .value(newUsername)
                .build();

        assertThat(inMemoryUserDetailsManager.userExists(oldUsername))
                .isTrue();
        assertThat(inMemoryUserDetailsManager.userExists(newUsername))
                .isFalse();

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/patch/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(inMemoryUserDetailsManager.userExists(oldUsername))
                .isFalse();
        assertThat(inMemoryUserDetailsManager.userExists(newUsername))
                .isTrue();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER password")
    public void whenAdminMemberPatchPasswordThenSuccess() {
        UserDetails existingUser = setupExistingUser();
        String newPassword = existingPassword + "_NEW";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.PASS.name().toLowerCase())
                .value(newPassword)
                .build();

        assertTrue(passwordEncoder.matches(
                existingPassword,
                inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername()).getPassword()));
        assertFalse(passwordEncoder.matches(
                newPassword,
                inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername()).getPassword()));

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/patch/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(passwordEncoder.matches(
                existingPassword,
                inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername()).getPassword()));
        assertTrue(passwordEncoder.matches(
                newPassword,
                inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername()).getPassword()));
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER role")
    public void whenAdminMemberPatchRoleThenSuccess() {
        UserDetails existingUser = setupExistingUser();
        String newRole = MyRole.LIBRARIAN.name();
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.ROLE.name().toLowerCase())
                .value(newRole)
                .build();

        String existingRole = inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername())
                .getAuthorities().iterator().next().getAuthority();
        assertThat(existingRole)
                .isEqualTo("ROLE_" + MyRole.MEMBER.name());

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/patch/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String patchedRole = inMemoryUserDetailsManager.loadUserByUsername(existingUser.getUsername())
                .getAuthorities().iterator().next().getAuthority();
        assertThat(patchedRole).isEqualTo("ROLE_" + newRole);
    }

    private UserDetails setupExistingUser() {
        UserDetails userDetails = User.withUsername(existingUsername)
                .passwordEncoder(passwordEncoder::encode)
                .password(existingPassword)
                .roles(MyRole.MEMBER.name())
                .build();
        inMemoryUserDetailsManager.createUser(userDetails);
        return userDetails;
    }

    private String authorizationToken(String userPass) {
        String base64EncodedToken = Base64.getEncoder().encodeToString(
                userPass.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64EncodedToken;
    }

    private String absoluteUrl(String path) {
        return "http://localhost:" + port + "/v1/librarian/admin" + path;
    }
}
