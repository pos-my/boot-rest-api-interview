package posmy.interview.boot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void getWithMemberAuthorizationThenReturnSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user002:pass"));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/get"),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getBody()).isEqualTo("success");
    }

    @Test
    public void getWithLibrarianAuthorizationThenReturnSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user001:pass"));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/get"),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getWithoutAuthorizationThenUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/get"),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getWithInvalidAuthorizationThenUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "anyInvalid");
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/get"),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private String authorizationToken(String userPass) {
        String base64EncodedToken = Base64.getEncoder().encodeToString(
                userPass.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64EncodedToken;
    }

    private String absoluteUrl(String path) {
        return "http://localhost:" + port + "/member" + path;
    }
}
