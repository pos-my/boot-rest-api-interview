package posmy.interview.boot.controller.integration;

import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.repos.MyUserRepository;
import posmy.interview.boot.util.Constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberAdminControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        resetUsersWithDefault();
    }
    private void resetUsersWithDefault() {
        Try.run(() -> myUserRepository.deleteAll());
        MyUser defaultLibrarian = MyUser.builder()
                .username(Constants.DEFAULT_LIBRARIAN_USERNAME)
                .password(passwordEncoder.encode(Constants.DEFAULT_LIBRARIAN_PASSWORD))
                .authority(MyRole.LIBRARIAN.authority)
                .build();
        MyUser defaultMember = MyUser.builder()
                .username(Constants.DEFAULT_MEMBER_USERNAME)
                .password(passwordEncoder.encode(Constants.DEFAULT_MEMBER_PASSWORD))
                .authority(MyRole.MEMBER.authority)
                .build();
        myUserRepository.save(defaultLibrarian);
        myUserRepository.save(defaultMember);
    }

    @Test
    void selfDeleteThenReturnSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken(
                        Constants.DEFAULT_MEMBER_USERNAME + ":" + Constants.DEFAULT_MEMBER_PASSWORD));

        assertThat(myUserRepository.existsByUsername(Constants.DEFAULT_MEMBER_USERNAME))
                .isTrue();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/self"),
                HttpMethod.DELETE,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(myUserRepository.existsByUsername(Constants.DEFAULT_MEMBER_USERNAME))
                .isFalse();
    }

    private String authorizationToken(String userPass) {
        String base64EncodedToken = Base64.getEncoder().encodeToString(
                userPass.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64EncodedToken;
    }

    private String absoluteUrl(String path) {
        return "http://localhost:" + port + "/v1/member/admin" + path;
    }
}
