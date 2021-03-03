package posmy.interview.boot.controller;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.repos.MyUserRepository;

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
    private MyUserRepository myUserRepository;

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
        Try.run(() -> myUserRepository.deleteByUsername(existingUsername));
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add MEMBER")
    public void whenAdminMemberAddThenSuccess() {
        MemberAddRequest request = MemberAddRequest.builder()
                .user("user100")
                .pass("pass100")
                .email("abc@abc.co")
                .build();

        assertThat(myUserRepository.existsByUsername(request.getUser()))
                .isFalse();

        HttpEntity<MemberAddRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member"),
                HttpMethod.POST,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(myUserRepository.existsByUsername(request.getUser()))
                .isTrue();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add Duplicate MEMBER")
    public void givenDuplicateUserWhenAdminMemberAddThenError() {
        MyUser existingUser = setupExistingUser();
        MemberAddRequest request = MemberAddRequest.builder()
                .user(existingUser.getUsername())
                .pass(existingUser.getPassword())
                .build();

        assertThat(myUserRepository.existsByUsername(request.getUser()))
                .isTrue();

        HttpEntity<MemberAddRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member"),
                HttpMethod.POST,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER username")
    public void whenAdminMemberPatchUsernameThenSuccess() {
        MyUser existingUser = setupExistingUser();
        String oldUsername = existingUser.getUsername();
        String newUsername = oldUsername + "_NEW";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.USER.name().toLowerCase())
                .value(newUsername)
                .build();

        assertThat(myUserRepository.existsByUsername(oldUsername))
                .isTrue();
        assertThat(myUserRepository.existsByUsername(newUsername))
                .isFalse();

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(myUserRepository.existsByUsername(oldUsername))
                .isFalse();
        assertThat(myUserRepository.existsByUsername(newUsername))
                .isTrue();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER password")
    public void whenAdminMemberPatchPasswordThenSuccess() {
        MyUser existingUser = setupExistingUser();
        String newPassword = existingPassword + "_NEW";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.PASS.name().toLowerCase())
                .value(newPassword)
                .build();

        assertTrue(passwordEncoder.matches(
                existingPassword,
                myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow().getPassword()));
        assertFalse(passwordEncoder.matches(
                newPassword,
                myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow().getPassword()));

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(passwordEncoder.matches(
                existingPassword,
                myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow().getPassword()));
        assertTrue(passwordEncoder.matches(
                newPassword,
                myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow().getPassword()));
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER role")
    public void whenAdminMemberPatchRoleThenSuccess() {
        MyUser existingUser = setupExistingUser();
        MyRole newRole = MyRole.LIBRARIAN;
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.ROLE.name().toLowerCase())
                .value(newRole.name())
                .build();

        String existingRole = myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow()
                .getAuthority();
        assertThat(existingRole)
                .isEqualTo(MyRole.MEMBER.authority);

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getUsername()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String patchedRole = myUserRepository.findByUsername(existingUser.getUsername()).orElseThrow()
                .getAuthority();
        assertThat(patchedRole).isEqualTo(newRole.authority);
    }

    private MyUser setupExistingUser() {
        MyUser user = MyUser.builder()
                .username(existingUsername)
                .password(passwordEncoder.encode(existingPassword))
                .authority(MyRole.MEMBER.authority)
                .build();
        return myUserRepository.save(user);
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
