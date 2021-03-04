package posmy.interview.boot.controller.integration;

import io.vavr.control.Try;
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
import posmy.interview.boot.model.response.MemberGetResponse;
import posmy.interview.boot.repos.MyUserRepository;
import posmy.interview.boot.util.Constants;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibrarianAdminControllerIntegrationTest {

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
        resetUsersWithDefaultLibrarian();

        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken(
                        Constants.DEFAULT_LIBRARIAN_USERNAME + ":" + Constants.DEFAULT_LIBRARIAN_PASSWORD));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
    private void resetUsersWithDefaultLibrarian() {
        Try.run(() -> myUserRepository.deleteAll());
        MyUser defaultLibrarian = MyUser.builder()
                .username(Constants.DEFAULT_LIBRARIAN_USERNAME)
                .password(passwordEncoder.encode(Constants.DEFAULT_LIBRARIAN_PASSWORD))
                .authority(MyRole.LIBRARIAN.authority)
                .build();
        myUserRepository.save(defaultLibrarian);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add MEMBER")
    void whenAdminMemberAddThenSuccess() {
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
    void givenDuplicateUserWhenAdminMemberAddThenError() {
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
    void whenAdminMemberPatchUsernameThenSuccess() {
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
                absoluteUrl("/member/" + existingUser.getId()),
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
    void whenAdminMemberPatchPasswordThenSuccess() {
        MyUser existingUser = setupExistingUser();
        String newPassword = existingPassword + "_NEW";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.PASS.name().toLowerCase())
                .value(newPassword)
                .build();

        assertTrue(passwordEncoder.matches(
                existingPassword,
                myUserRepository.findById(existingUser.getId()).orElseThrow().getPassword()));
        assertFalse(passwordEncoder.matches(
                newPassword,
                myUserRepository.findById(existingUser.getId()).orElseThrow().getPassword()));

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getId()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(passwordEncoder.matches(
                existingPassword,
                myUserRepository.findById(existingUser.getId()).orElseThrow().getPassword()));
        assertTrue(passwordEncoder.matches(
                newPassword,
                myUserRepository.findById(existingUser.getId()).orElseThrow().getPassword()));
    }

    @Test
    @DisplayName("Users with role LIBRARIAN patch MEMBER role")
    void whenAdminMemberPatchRoleThenSuccess() {
        MyUser existingUser = setupExistingUser();
        MyRole newRole = MyRole.LIBRARIAN;
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.ROLE.name().toLowerCase())
                .value(newRole.name())
                .build();

        String existingRole = myUserRepository.findById(existingUser.getId()).orElseThrow()
                .getAuthority();
        assertThat(existingRole)
                .isEqualTo(MyRole.MEMBER.authority);

        HttpEntity<MemberPatchRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getId()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String patchedRole = myUserRepository.findById(existingUser.getId()).orElseThrow()
                .getAuthority();
        assertThat(patchedRole).isEqualTo(newRole.authority);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN get all MEMBER")
    void whenAdminMemberGetThenSuccess() {
        MemberGetResponse expectedResponse = setupGetExistingWithExpected();

        HttpEntity<MemberAddRequest> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<MemberGetResponse> responseEntity = restTemplate.exchange(
                absoluteUrl("/member"),
                HttpMethod.GET,
                httpEntity,
                MemberGetResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN delete MEMBER")
    void whenAdminMemberDeleteByIdThenSuccess() {
        MyUser existingUser = setupExistingUser();

        assertThat(myUserRepository.existsById(existingUser.getId()))
                .isTrue();

        HttpEntity<Void> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + existingUser.getId()),
                HttpMethod.DELETE,
                httpEntity,
                Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(myUserRepository.existsById(existingUser.getId()))
                .isFalse();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN delete non-existing MEMBER")
    void givenNonExistingIdWhenAdminMemberDeleteUserByIdThenDoNothing() {
        Long deleteId = 999L;

        assertThat(myUserRepository.existsById(deleteId))
                .isFalse();

        HttpEntity<Void> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                absoluteUrl("/member/" + deleteId),
                HttpMethod.DELETE,
                httpEntity,
                Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(myUserRepository.existsById(deleteId))
                .isFalse();
    }

    private MemberGetResponse setupGetExistingWithExpected() {
        MyUser existingUser1 = setupExistingUser(
                "userGet001", "passGet001", MyRole.MEMBER.authority);
        MyUser existingUser2 = setupExistingUser(
                "userGet002", "passGet002", MyRole.MEMBER.authority);
        MyUser existingUser3 = setupExistingUser(
                "userGet003", "passGet003", MyRole.LIBRARIAN.authority);
        MyUser existingUser4 = setupExistingUser(
                "userGet004", "passGet004", MyRole.MEMBER.authority);
        assertThat(myUserRepository.existsByUsername(existingUser1.getUsername()))
                .isTrue();
        assertThat(myUserRepository.existsByUsername(existingUser2.getUsername()))
                .isTrue();
        assertThat(myUserRepository.existsByUsername(existingUser3.getUsername()))
                .isTrue();
        assertThat(myUserRepository.existsByUsername(existingUser4.getUsername()))
                .isTrue();

        MemberGetResponse.UserDetailsDto member1 = MemberGetResponse.UserDetailsDto.builder()
                .id(existingUser1.getId())
                .username(existingUser1.getUsername())
                .email(existingUser1.getEmail())
                .build();
        MemberGetResponse.UserDetailsDto member2 = MemberGetResponse.UserDetailsDto.builder()
                .id(existingUser2.getId())
                .username(existingUser2.getUsername())
                .email(existingUser2.getEmail())
                .build();
        MemberGetResponse.UserDetailsDto member3 = MemberGetResponse.UserDetailsDto.builder()
                .id(existingUser4.getId())
                .username(existingUser4.getUsername())
                .email(existingUser4.getEmail())
                .build();
        return MemberGetResponse.builder()
                .members(new ArrayList<>(List.of(member1, member2, member3)))
                .build();
    }

    private MyUser setupExistingUser() {
        return setupExistingUser(existingUsername, existingPassword, MyRole.MEMBER.authority);
    }

    private MyUser setupExistingUser(String username, String password, String authority) {
        MyUser user = MyUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authority(authority)
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
