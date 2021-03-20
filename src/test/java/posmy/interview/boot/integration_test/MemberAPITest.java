package posmy.interview.boot.integration_test;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import posmy.interview.boot.BaseAPIIntegrationTest;
import posmy.interview.boot.dto.*;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.fixture.BookBuilder;
import posmy.interview.boot.fixture.MemberBuilder;
import posmy.interview.boot.repository.MemberRepository;

import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class MemberAPITest extends BaseAPIIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    /*
        Librarian
     */
    @Test
    public void librarianCreateMemberTest() {
        headers.add(AUTHORIZATION, getLibrarian().getToken());
        MemberCreateDto createDto = MemberBuilder.sample().buildCreateDto();
        ResponseEntity<MemberDto> response = restTemplate.exchange("/member", POST, new HttpEntity(createDto, headers), MemberDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).id > 0);
    }

    @Test
    public void librarianUpdateMemberTest() {

        MemberDto memberDto = genericCreateMember();

        MemberUpdateDto updateDto = MemberBuilder.sample().setPhoneNumber("60133461582").buildUpdateDto();
        ResponseEntity<MemberUpdateDto> response2 = restTemplate.exchange("/member/" + memberDto.id, PUT, new HttpEntity(updateDto, headers), MemberUpdateDto.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(Objects.requireNonNull(response2.getBody()).phoneNumber, updateDto.phoneNumber);
    }

    @Test
    public void librarianDeleteMemberTest() {

        MemberDto memberDto = genericCreateMember();

        ResponseEntity<Void> response2 = restTemplate.exchange("/member/" + memberDto.id, DELETE, new HttpEntity(headers), Void.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        ResponseEntity<MemberDto> response3 = restTemplate.exchange("/member" + memberDto.id, GET, new HttpEntity(headers), MemberDto.class);
        assertEquals(NOT_FOUND, response3.getStatusCode());
    }

    /*
        Member
     */

    @Test
    public void memberCreateMemberTest() {
        headers.add(AUTHORIZATION, getMember().getToken());
        MemberCreateDto createDto = MemberBuilder.sample().buildCreateDto();
        ResponseEntity<MemberDto> response = restTemplate.exchange("/member", POST, new HttpEntity(createDto, headers), MemberDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void memberUpdateMemberTest() {

        MemberDto memberDto = genericCreateMember();

        headers.set(AUTHORIZATION, getMember().getToken());
        MemberUpdateDto updateDto = MemberBuilder.sample().setPhoneNumber("60133461582").buildUpdateDto();
        ResponseEntity<MemberUpdateDto> response2 = restTemplate.exchange("/member/" + memberDto.id, PUT, new HttpEntity(updateDto, headers), MemberUpdateDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    public void memberDeleteMemberTest() {

        MemberDto memberDto = genericCreateMember();

        headers.set(AUTHORIZATION, getMember().getToken());
        ResponseEntity<Void> response2 = restTemplate.exchange("/member/" + memberDto.id, DELETE, new HttpEntity(headers), Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    public void memberDeleteSelfTest() {

        MemberDto memberDto = genericCreateMember();

        // because dto didn't return token we need to bypass them
        Member newMember = memberRepository.findById(memberDto.id).orElseThrow();

        headers.set(AUTHORIZATION, newMember.getToken());
        ResponseEntity<Void> response2 = restTemplate.exchange("/member/self", DELETE, new HttpEntity(headers), Void.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        headers.set(AUTHORIZATION, getLibrarian().getToken());
        ResponseEntity<MemberDto> response3 = restTemplate.exchange("/member" + memberDto.id, GET, new HttpEntity(headers), MemberDto.class);
        assertEquals(NOT_FOUND, response3.getStatusCode());
    }

    private MemberDto genericCreateMember() {
        headers.add(AUTHORIZATION, getLibrarian().getToken());
        MemberCreateDto createDto = MemberBuilder.sample().buildCreateDto();
        ResponseEntity<MemberDto> response = restTemplate.exchange("/member", POST, new HttpEntity(createDto, headers), MemberDto.class);
        MemberDto bookDto = response.getBody();
        assert bookDto != null;
        return bookDto;
    }
}
