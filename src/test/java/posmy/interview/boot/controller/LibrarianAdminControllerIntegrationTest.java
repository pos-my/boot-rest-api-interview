package posmy.interview.boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.model.request.MemberAddRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibrarianAdminControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private HttpHeaders headers;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user001:pass"));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
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

    private UserDetails setupExistingUser() {
        UserDetails userDetails = User.withUsername("user999")
                .password("pass")
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
