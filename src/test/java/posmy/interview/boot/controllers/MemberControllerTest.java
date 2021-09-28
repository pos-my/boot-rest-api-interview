package posmy.interview.boot.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.book.BookDto;
import posmy.interview.boot.models.dtos.book.BookStatus;
import posmy.interview.boot.models.dtos.member.MemberDto;
import posmy.interview.boot.models.dtos.member.UserRole;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class MemberControllerTest {

    private String baseUrl;
    private String librarianEmail;
    private Member testMember;
    private MemberDto testMemberDto;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        librarianEmail = "admin@email.com";
        testMember = new Member();
        testMember.setMemberId(1);
        testMember.setFirstName("Mr");
        testMember.setLastName("Librarian");
        testMember.setPhone("01122334455");
        testMember.setEmail("admin@email.com");
        testMember.setPassword("123456");
        testMember.setUserRole(UserRole.LIBRARIAN);
        testMemberDto = new MemberDto();
        testMemberDto.setFirstName("New");
        testMemberDto.setLastName("Member");
        testMemberDto.setPhone("01122334455");
        testMemberDto.setEmail("member@gmail.com");
        testMemberDto.setUserRole(UserRole.MEMBER);
    }

    @Test
    void getMember() {
    }
    void getMember_success() throws URISyntaxException {
        var uri = new URI(baseUrl + "/member/" + testMember.getMemberId());
        var headers = new HttpHeaders();
        headers.add("Email", librarianEmail);
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    void getMembers() {
    }

    @Test
    void addMembers() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void deleteMember() {
    }
}