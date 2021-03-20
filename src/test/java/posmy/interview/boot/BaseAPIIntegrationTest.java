package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.fixture.LibrarianBuilder;
import posmy.interview.boot.fixture.MemberBuilder;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BaseAPIIntegrationTest extends BaseTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    protected HttpHeaders headers;

    protected Librarian librarian;

    protected Member member;

    @BeforeEach
    public void setupBefore() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // fixme: bypass our password hash algorithm and too ugly
        Librarian librarian = LibrarianBuilder.sample().build();
        this.librarian = librarianRepository.save(librarian);

        Member member = MemberBuilder.sample().generateToken().build();
        this.member = memberRepository.save(member);
    }

    protected Librarian getLibrarian() {
        return librarian;
    }

    protected Member getMember() {
        return member;
    }
}
